package com.example.cuckoolandback.mafia.controller;

import com.example.cuckoolandback.majority.common.Message;
import com.example.cuckoolandback.majority.common.exception.CustomException;
import com.example.cuckoolandback.majority.common.exception.ErrorCode;
import com.example.cuckoolandback.mafia.dto.GameMessage;
import com.example.cuckoolandback.mafia.domain.Player;
import com.example.cuckoolandback.mafia.repository.PlayerRepository;
import com.example.cuckoolandback.mafia.domain.Role;
import com.example.cuckoolandback.room.domain.Participant;
import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.room.repository.ParticipantRepository;
import com.example.cuckoolandback.room.repository.RoomRepository;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MafiaController {

    private final RoomRepository roomRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ParticipantRepository participantRepository;
    private final PlayerRepository playerRepository;
    private final MemberRepository memberRepository;


    @MessageMapping("/mafia/{roomId}")
    public void gameMessageProxy(@Payload GameMessage message) throws JsonProcessingException {
        if (GameMessage.MessageType.ENTER.equals(message.getType())) {
            gameEnter(message);
        }
        if (GameMessage.MessageType.EXIT.equals(message.getType())) {
            gameExit(message);
        }
        if (GameMessage.MessageType.START.equals(message.getType())) {
            gameStart(message);
        }
        if (GameMessage.MessageType.CHAT.equals(message.getType())) {
            gameChat(message);
        }
        if (GameMessage.MessageType.MISSION.equals(message.getType())) {
            gameMission(message);
        }
        if (GameMessage.MessageType.FAILMISSION.equals(message.getType())) {
            gameFailMission(message);
        }
        if (GameMessage.MessageType.VOTE.equals(message.getType())) {
            gameVote(message);
        }
        if (GameMessage.MessageType.PICK.equals(message.getType())) {
            gamePick(message);
        }
        if (GameMessage.MessageType.ENDCHECK.equals(message.getType())) {
            gameEndCheck(message);
        }
    }



    @Transactional
    public void gameEnter(GameMessage message) {
        // ??????????????? sender??? memberId??? ?????????.
        Long roomID = message.getRoomId();
        Room room = roomRepository.findById(roomID).orElseThrow(
                () -> new CustomException(ErrorCode.ROOMS_NOT_FOUND)
        );
        participantRepository.save(
                Participant.builder()
                        .id(message.getSender())
                        .roomId(roomID)
                        .build());

        List<Participant> participants = participantRepository.findByRoomIdOrderByCreatedDate(roomID);

        StringBuilder content= new StringBuilder();
        for(Participant participant:participants){
            content.append(participant.getId()).append(" ");
        }

        GameMessage gameMessage = new GameMessage();
        gameMessage.setRoomId(roomID);
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        gameMessage.setContent(content.toString());
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
    }

    @Transactional
    public void gameExit(GameMessage message) {
        Long roomID = message.getRoomId();
        roomRepository.findById(roomID).orElseThrow(() -> new CustomException(ErrorCode.ROOMS_NOT_FOUND));
        participantRepository.deleteById(message.getSender());

        List<Participant> participants = participantRepository.findByRoomIdOrderByCreatedDate(roomID);

        StringBuilder content= new StringBuilder();
        for(Participant participant:participants){
            content.append(participant.getId()).append(" ");
        }

        GameMessage gameMessage = new GameMessage();
        gameMessage.setRoomId(roomID);
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        gameMessage.setContent(content.toString());
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
    }

    @Transactional
    public void gameStart(GameMessage message) {
        // ROOM ????????? ????????? ?????? ???????????? ??????(???????????? ?????? - ???????????? ?????? ??????, ??????????????? ????????????)
        Long roomID = message.getRoomId();
        List<Participant> participants = participantRepository.findByRoomIdOrderByCreatedDate(roomID);
        int cnt = participants.size();
        if(cnt <5){
            throw new CustomException(ErrorCode.PLAYERS_LACK);
        }
        List<Role> roleList =
                Arrays.asList(
                        Role.MAFIA,
                        Role.CITIZEN,
                        Role.CITIZEN,
                        Role.CITIZEN,
                        Role.CITIZEN,
                        Role.POLICE,
                        Role.MAFIA,
                        Role.DOCTOR,
                        Role.MAFIA,
                        Role.CITIZEN);
        List<Role> roles = roleList.subList(0,cnt);
        Collections.shuffle(roles);

        List<Player> playerList = new ArrayList<>();
        for(int i=0;i<participants.size();i++){
            Player player = new Player(participants.get(i));
            player.setRole(roles.get(i));
            if ((player.getRole().equals(Role.DOCTOR)) || (player.getRole().equals(Role.POLICE))) {
                player.setHaveRight(true);
            }
            playerRepository.save(player);
            playerList.add(player);
        }
        List<String> keywords =
                Arrays.asList("??????","??????","??????","??????","???","??????","??????","??????","???","??????","??????","??????","??????");
        Collections.shuffle(keywords);
        int order = 0;
        for(Player player:playerList){
            GameMessage gameMessage = new GameMessage();
            gameMessage.setRoomId(roomID);
            gameMessage.setSender(player.getMemberId());
            gameMessage.setType(GameMessage.MessageType.SERVER);

            StringBuilder content= new StringBuilder();
            if(player.getRole().equals(Role.MAFIA)){
                content.append("?????? ?????????").append(", ").append(keywords.get(order++));
            }
            gameMessage.setContent(player.getMemberId()+":"+ player.getRole()+":"+content);
            messagingTemplate.convertAndSend("/topic/mafia/" +message.getRoomId(), gameMessage);
        }
        GameMessage gameMessage = new GameMessage();
        gameMessage.setRoomId(roomID);
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        gameMessage.setContent("DAY START");
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
    }

    private void gameChat(GameMessage message) {
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), message);
    }

    @Transactional
    public void gameMission(GameMessage message) {
        // ?????? ????????? ????????? ?????? ????????? ??? ????????? ?????? ??????
        // ?????? ?????? ??? ???????????? ????????? update
        // ?????? ?????? ??? ?????? ????????? ??????.
        Player mafia = playerRepository.findByMemberId(message.getSender()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if(!mafia.getRole().equals(Role.MAFIA)){
            throw new CustomException(ErrorCode.NOT_MAFIA);
        }
        GameMessage gameMessage = new GameMessage();
        gameMessage.setSender(mafia.getMemberId());
        mafia.setMissionCnt(mafia.getMissionCnt()+1);
        playerRepository.save(mafia);
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setContent(mafia.getMissionCnt()+"??? ??????");
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);

        if(mafia.getMissionCnt()==2){
            mafia.setHaveRight(true);
            playerRepository.save(mafia);
            gameMessage.setContent("MISSION COMPLETE");
            gameMessage.setType(GameMessage.MessageType.SERVER);
            messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
        }
    }

    private void gameFailMission(GameMessage message) {
        // ??????????????? sender??? ????????? ????????? ??????
        GameMessage gameMessage = new GameMessage();
        List<Player> players = playerRepository.findByRoleAndRoomIdAndIsDeadFalse(Role.CITIZEN, message.getRoomId());
        Collections.shuffle(players);
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(players.get(0).getMemberId());
        gameMessage.setType(GameMessage.MessageType.SERVER);
        gameMessage.setContent("???????????? " + message.getSender()+" ?????????.");
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
    }

    private void gameVote(GameMessage message) {

        Player player = playerRepository.findByMemberId(message.getContent()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        player.setVoteCnt(player.getVoteCnt()+1);
        playerRepository.save(player);
        // ??????????????? ?????? ?????? ?????? ??? ?????? ??????
        GameMessage gameMessage = new GameMessage();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setContent("?????? ??????");
        int totalVote = 0;
        int max = 0;
        List<String> result = new ArrayList<>();
        List<Player> players = playerRepository.findByRoomId(message.getRoomId());
        for (Player player1 : players) {
            if (max < player1.getVoteCnt()) {
                max = player1.getVoteCnt();
                result.clear();
                result.add(player1.getMemberId());
            } else if (max == player1.getVoteCnt()) {
                result.add(player1.getMemberId());
            }
            totalVote = totalVote + player1.getVoteCnt();
        }
        if (totalVote == players.size()) {
            gameMessage.setContent(String.join(", ", result));
            if (result.size() == 1) {
                Player player2 = playerRepository.findByMemberId(result.get(0)).orElseThrow(
                        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
                );
                player2.setDead(true);
                playerRepository.save(player2);
                gameMessage.setContent(player2.getMemberId()+"?????? ?????????????????????.");
            }
        }
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
    }

    @Transactional
    public void gamePick(GameMessage message) {
        // ???????????? ??? ?????? ?????? ??? ?????? ?????? ????????? ??????
        // ?????? ????????? content??? ?????????.
        // ????????? ??? ?????? ??? ?????? ????????? ?????? ?????? ??? ?????? ?????? ????????? ??????
        // ????????? ??? ?????? ??? ?????? ?????? ???????????? ?????? ??? ???????????? ????????? ??????
        // ??? ??? ????????? ???????????? DB ??????
        // ?????? ?????? ?????? ??????(????????? 0???, ????????? ???????????? ??????)
        Player picker = playerRepository.findByMemberId(message.getSender()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Player object = playerRepository.findByMemberId(message.getContent()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Role type = picker.getRole();
        StringBuilder content= new StringBuilder();
        GameMessage gameMessage = new GameMessage();
        gameMessage.setSender(picker.getMemberId());
        switch (type){
            case MAFIA:
                if(picker.isHaveRight()){
                    object.setDead(true);
                    content.append(object.getMemberId()).append("?????? ?????????????????????.");
                    gameMessage.setRoomId(message.getRoomId());
                    gameMessage.setType(GameMessage.MessageType.SERVER);
                    gameMessage.setContent(content.toString());
                    messagingTemplate.convertAndSend("/topic/mafia/"+message.getRoomId(), gameMessage);
                    picker.setHaveRight(false);
                    playerRepository.save(picker);
                }
                break;
            case POLICE:
                if(picker.isHaveRight()){
                    content.append(object.getMemberId()).append("?????? ????????? ").append(object.getRole()).append("?????????.");
                    gameMessage.setRoomId(message.getRoomId());
                    gameMessage.setType(GameMessage.MessageType.SERVER);
                    gameMessage.setContent(content.toString());
                    messagingTemplate.convertAndSend("/topic/mafia/"+message.getRoomId(), gameMessage);
                    picker.setHaveRight(false);
                }
                break;
            case DOCTOR:
                if(picker.isHaveRight()){
                    if(object.isDead()) {
                        object.setDead(false);
                        content.append(object.getMemberId()).append("?????? ???????????????.");
                        gameMessage.setRoomId(message.getRoomId());
                        gameMessage.setType(GameMessage.MessageType.SERVER);
                        gameMessage.setContent(content.toString());
                        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
                        picker.setHaveRight(false);
                    }
                }
                break;
        }
    }


    @Transactional
    public void gameEndCheck(GameMessage message) {
        // ??????????????? ???????????? ??? ??????
        // ???????????? ?????? ?????????
        // ??? ?????? ?????????
        // ????????? ?????? ???????????? member?????? DB??????
        List<Player> mafias = playerRepository.findByRoleAndRoomIdAndIsDeadFalse(Role.MAFIA,message.getRoomId());
        List<Role> roles = new ArrayList<>();
        roles.add(Role.MAFIA);
        List<Player> notMafias = playerRepository.findByRoleNotInAndRoomIdAndIsDeadFalse(roles,message.getRoomId());

        if(mafias.size()==0){
            GameMessage gameMessage = new GameMessage();
            gameMessage.setRoomId(message.getRoomId());
            gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
            gameMessage.setContent("?????? ??????");
            gameMessage.setType(GameMessage.MessageType.SERVER);
            messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
            for(Player player:notMafias){
                Member member = memberRepository.findByMemberId(player.getMemberId()).orElseThrow(
                        ()->new CustomException(ErrorCode.USER_NOT_FOUND)
                );
                String[] winNums = member.getMafiaWinNum().split(" ");
                switch (player.getRole()) {
                    case CITIZEN:
                        winNums[0] = String.valueOf(Integer.parseInt(winNums[0]) + 1);
                        break;
                    case POLICE:
                        winNums[2] = String.valueOf(Integer.parseInt(winNums[2]) + 1);
                        break;
                    case DOCTOR:
                        winNums[3] = String.valueOf(Integer.parseInt(winNums[3]) + 1);
                        break;
                    default:
                        break;
                }
                String result = String.join(" ", winNums);
                member.setMafiaWinNum(result);
                memberRepository.save(member);
            }
            List<Player> players = playerRepository.findByRoomId(message.getRoomId());
            for(Player player:players){
                playerRepository.delete(player);
            }
        }else if(mafias.size()>=notMafias.size()){
            GameMessage gameMessage = new GameMessage();
            gameMessage.setRoomId(message.getRoomId());
            gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
            gameMessage.setContent("????????? ??????");
            gameMessage.setType(GameMessage.MessageType.SERVER);
            messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
            for(Player player:mafias){
                Member member = memberRepository.findByMemberId(player.getMemberId()).orElseThrow(
                        ()->new CustomException(ErrorCode.USER_NOT_FOUND)
                );

                String[] winNums = member.getMafiaWinNum().split(" ");
                winNums[1] = String.valueOf(Integer.parseInt(winNums[1]) + 1);
                String result = String.join(" ", winNums);
                member.setMafiaWinNum(result);
                memberRepository.save(member);
            }
            List<Player> players = playerRepository.findByRoomId(message.getRoomId());
            for(Player player:players){
                playerRepository.delete(player);
            }
        }else{
            GameMessage gameMessage = new GameMessage();
            gameMessage.setRoomId(message.getRoomId());
            gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
            gameMessage.setType(GameMessage.MessageType.SERVER);
            gameMessage.setContent("?????? ??????");
            messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
        }
    }
}