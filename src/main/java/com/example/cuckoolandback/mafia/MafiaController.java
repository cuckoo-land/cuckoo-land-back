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
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MafiaController {

    private final RoomRepository roomRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;


    @MessageMapping("/mafia/{roomId}")
    public void gameMessageProxy(@Payload GameMessage message) throws JsonProcessingException {
        if (GameMessage.MessageType.ENTER.equals(message.getType())) {
            gameEnter(message);
        }
        if (GameMessage.MessageType.START.equals(message.getType())) {
            gameStart(message);
        }
        if (GameMessage.MessageType.CHAT.equals(message.getType())) {
            gameChat(message);
        }
        if (GameMessage.MessageType.MISSON.equals(message.getType())) {
            gameMission(message);
        }
        if (GameMessage.MessageType.VOTE.equals(message.getType())) {
            gameVote(message);
        }
        if (GameMessage.MessageType.PICK.equals(message.getType())) {
            gamePick(message);
        }
        if (GameMessage.MessageType.END.equals(message.getType())) {
            gameEnd(message);
        }
    }

    @Transactional
    public void gameEnter(GameMessage message) {
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

    private void gameStart(GameMessage message) {
        // ROOM 접속한 사람들 정보 취합하여 리턴(플레이어 정보 - 각각에게 역할 전달, 마피아에게 미션전달)
        // 화상 연결
        // 낮 시작 알림
        // 플레이어 DB 저장
        // 게임 테이블 저장
    }

    private void gameChat(GameMessage message) {
        // 오고가는 메시지 일반 송수신 진행
    }

    private void gameMission(GameMessage message) {
        // 해당 요청올 때마다 미션 카운팅 후 카운팅 숫자 전달
        // 미션 수행 시 성공 메시지 전달
        // 미션 수행 시 플레이어 상태값 update
    }

    private void gameVote(GameMessage message) {
        // 프론트에서 전체 투표 취합 후 전달 시
        // 낮에서 밤으로 상태 변경 메시지 전달
    }

    private void gamePick(GameMessage message) {
        // 마피아일 때 시민 저격 후 저격 완료 메시지 전달
        // 경찰일 때 저격 후 저격 상대방 정보 확인 후 상대 역할 메시지 전달
        // 의사일 때 저격 후 기존 저격 대상여부 확인 후 회생여부 메시지 전달
        // 위 세 상태값 업데이트 DB 저장
    }

    private void gameEnd(GameMessage message) {
        // player 정보 초기화
        // 방 정보 초기화
        // 승률과 승점 계산하여 member정보 DB반영
    }
}
