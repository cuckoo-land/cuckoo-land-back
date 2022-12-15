package com.example.cuckoolandback.mafia;

import com.example.cuckoolandback.common.Message;
import com.example.cuckoolandback.common.exception.CustomException;
import com.example.cuckoolandback.common.exception.ErrorCode;
import com.example.cuckoolandback.room.domain.Participant;
import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.room.repository.ParticipantRepository;
import com.example.cuckoolandback.room.repository.RoomRepository;
import com.example.cuckoolandback.user.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import javax.transaction.Transactional;
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
        if (GameMessage.MessageType.LEAVE.equals(message.getType())) {
            gameLeave(message);
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
        if (GameMessage.MessageType.DAY.equals(message.getType())) {
            gameDay(message);
        }
        if (GameMessage.MessageType.END.equals(message.getType())) {
            gameEnd(message);
        }
    }



    @Transactional
    public void gameEnter(GameMessage message) {
        // 프론트에서 sender에 memberId를 넘겨줌.
        Long roomID = message.getRoomId();
        Room room = roomRepository.findById(roomID).orElseThrow(
                () -> new CustomException(ErrorCode.ROOMS_NOT_FOUND)
        );
        participantRepository.save(
                Participant.builder()
                        .id(message.getSender())
                        .roomId(roomID)
                        .build());

        List<Participant> participants = participantRepository.findByRoomId(roomID);

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
    public void gameLeave(GameMessage message) {
        Long roomID = message.getRoomId();
        roomRepository.findById(roomID).orElseThrow(() -> new CustomException(ErrorCode.ROOMS_NOT_FOUND));
        participantRepository.deleteById(message.getSender());

        List<Participant> participants = participantRepository.findByRoomId(roomID);

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
        // ROOM 접속한 사람들 정보 취합하여 리턴(플레이어 정보 - 각각에게 역할 전달, 마피아에게 미션전달)
        Long roomID = message.getRoomId();
        List<Participant> participants = participantRepository.findByRoomId(roomID);
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
            if(player.getRole().equals(Role.MAFIA)){
                player.setHaveRight(false);
            }
            playerRepository.save(player);
            playerList.add(player);
        }

        for(Player player:playerList){
            GameMessage gameMessage = new GameMessage();
            gameMessage.setRoomId(roomID);
            gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
            gameMessage.setType(GameMessage.MessageType.SERVER);

            StringBuilder content= new StringBuilder();
            if(player.getRole().equals(Role.MAFIA)){
                content.append("미션 키워드").append(", ").append("지금");
            }
            gameMessage.setContent(player.getMemberId()+":"+ player.getRole()+":"+content);
            messagingTemplate.convertAndSend("/topic/mafia/" + player.getMemberId(), gameMessage);
        }
        GameMessage gameMessage = new GameMessage();
        gameMessage.setRoomId(roomID);
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        gameMessage.setContent("DAY START");
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), gameMessage);
    }

    private void gameDay(GameMessage message) {
        // 각각의 picker의 권한 새로 부여
        GameMessage gameMessage = new GameMessage();
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
        // 해당 요청올 때마다 미션 카운팅 후 카운팅 숫자 전달
        // 미션 수행 시 플레이어 상태값 update
        // 미션 수행 시 성공 메시지 전달.. 실패하면 어디로 가지..
        Player mafia = playerRepository.findByMemberId(message.getSender()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if(!mafia.getRole().equals(Role.MAFIA)){
            throw new CustomException(ErrorCode.NOT_MAFIA);
        }
        GameMessage gameMessage = new GameMessage();
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        mafia.setMissionCnt(mafia.getMissionCnt()+1);
        playerRepository.save(mafia);
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setContent(mafia.getMissionCnt()+"번 성공");
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + mafia.getMemberId(), gameMessage);

        if(mafia.getMissionCnt()==3){
            mafia.setHaveRight(true);
            playerRepository.save(mafia);
            gameMessage.setContent("MISSION COMPLETE");
            gameMessage.setType(GameMessage.MessageType.SERVER);
            messagingTemplate.convertAndSend("/topic/mafia/" + mafia.getMemberId(), gameMessage);
        }
    }

    private void gameFailMission(GameMessage message) {
        GameMessage gameMessage = new GameMessage();

        List<Player> players = playerRepository.findByRoleAndRoomId(Role.CITIZEN, message.getRoomId());
        Collections.shuffle(players);

        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setType(GameMessage.MessageType.SERVER);
        gameMessage.setContent("마피아는 " + gameMessage.getSender()+" 입니다.");
        messagingTemplate.convertAndSend("/topic/mafia/" + players.get(0).getMemberId(), gameMessage);
    }


    private void gameVote(GameMessage message) {

        Player player = playerRepository.findByMemberId(message.getContent()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        player.setVoteCnt(player.getVoteCnt()+1);
        playerRepository.save(player);
        // 프론트에서 전체 투표 취합 후 대상 응답
        GameMessage gameMessage = new GameMessage();
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
                gameMessage.setContent(player2.getMemberId()+"님이 사망하셨습니다.");
            }
        }

        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        gameMessage.setType(GameMessage.MessageType.SERVER);
        messagingTemplate.convertAndSend("/topic/mafia/" + message.getRoomId(), message);
    }

    @Transactional
    public void gamePick(GameMessage message) {
        // 마피아일 때 시민 저격 후 저격 완료 메시지 전달
        // 저격 대상을 content에 넘겨줌.
        // 경찰일 때 저격 후 저격 상대방 정보 확인 후 상대 역할 메시지 전달
        // 의사일 때 저격 후 기존 저격 대상여부 확인 후 회생여부 메시지 전달
        // 위 세 상태값 업데이트 DB 저장
        Player picker = playerRepository.findByMemberId(message.getSender()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Player object = playerRepository.findByMemberId(message.getContent()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Role type = picker.getRole();
        StringBuilder content= new StringBuilder();
        GameMessage gameMessage = new GameMessage();
        gameMessage.setSender(Message.SERVER_NOTICE.getMsg());
        switch (type){
            case MAFIA:
                if(picker.isHaveRight()){
                    object.setDead(true);
                    content.append(object.getMemberId()).append("님을 저격하였습니다.");
                    gameMessage.setRoomId(message.getRoomId());
                    gameMessage.setType(GameMessage.MessageType.SERVER);
                    gameMessage.setContent(content.toString());
                    messagingTemplate.convertAndSend("/topic/mafia/"+picker.getMemberId(), gameMessage);
                    picker.setHaveRight(false);
                    playerRepository.save(picker);
                }
                break;
            case POLICE:
                if(picker.isHaveRight()){
                    content.append(object.getMemberId()).append("님의 정체는 ").append(object.getRole()).append("입니다.");
                    messagingTemplate.convertAndSend("/topic/mafia/"+picker.getMemberId(), gameMessage);
                }
                break;
            case DOCTOR:
                if(picker.isHaveRight()){
                    if(object.isDead()) {
                        content.append(object.getMemberId()).append("님을 살렸습니다.");
                        messagingTemplate.convertAndSend("/topic/mafia/" + picker.getMemberId(), gameMessage);
                    }
                }
                break;
        }
    }


    private void gameEnd(GameMessage message) {
        // player 정보 초기화
        // 방 정보 초기화
        // 승률과 승점 계산하여 member정보 DB반영
    }
}