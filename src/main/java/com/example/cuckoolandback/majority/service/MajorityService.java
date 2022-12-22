package com.example.cuckoolandback.majority.service;

import com.example.cuckoolandback.majority.common.exception.CustomException;
import com.example.cuckoolandback.majority.common.exception.ErrorCode;
import com.example.cuckoolandback.majority.domain.Majority;
import com.example.cuckoolandback.majority.domain.Picture;
import com.example.cuckoolandback.majority.domain.Vote;
import com.example.cuckoolandback.majority.domain.Vs;
import com.example.cuckoolandback.majority.dto.*;
import com.example.cuckoolandback.majority.repository.MajorityRepository;
import com.example.cuckoolandback.majority.repository.PictureRepository;
import com.example.cuckoolandback.majority.repository.VoteRepository;
import com.example.cuckoolandback.majority.repository.VsRepository;
import com.example.cuckoolandback.room.domain.Participant;
import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.room.domain.RoomStatus;

import com.example.cuckoolandback.room.repository.ParticipantRepository;
import com.example.cuckoolandback.room.repository.RoomRepository;
import com.example.cuckoolandback.room.service.RoomService;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.dto.MemberResponseDto;
import com.example.cuckoolandback.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorityService {

    private final SimpMessageSendingOperations sendingOperations;
    private final MajorityRepository majorityRepository;
    private final PictureRepository pictureRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final VsRepository vsRepository;
    private final VoteRepository voteRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;

    public Optional<Majority> getRandom() {
        return majorityRepository.findMajorityByRandom();
    }

    final String PATH = "/topic/majority/";

    public Room findRoom(Long roomId, SendType type) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isEmpty()) {
            MajorityMessage message = MajorityMessage.builder()
                .type(type)
                .message("NOT FOUND ROOM").build();
            sendingOperations.convertAndSend(PATH + roomId, message);
            throw new CustomException(ErrorCode.ROOMS_NOT_FOUND);
        }

        return roomOptional.get();
    }

    @Transactional
    public void start(MajorityRequestDto requestDto) {
        Optional<Majority> majority;
        List<Picture> pictures;
        int roundTotal = 0;
        Room room = findRoom(requestDto.getRoomId(), SendType.START);
        switch (requestDto.getRound()) {
            case THIRTYTWO:
                roundTotal = 32;
                break;

            case SIXTYFOUR:
                roundTotal = 64;
                break;
        }
        if (requestDto.getGameType() == 0) {
            majority = getRandom();
        } else {
            majority = majorityRepository.findById(requestDto.getGameType());
        }

        if (majority.isPresent()) {
            pictures = pictureRepository.findPicturesByRandom(majority.get().getId());
            List<Vs> vsList = new ArrayList<>();
            for (int i = 0; i < roundTotal / 2; i++) {
                Vs vs = new Vs();
                vs.setItemId1(pictures.get(2 * i).getId());
                vs.setItemId2(pictures.get(2 * i + 1).getId());
                vs.setRoundNum(roundTotal - i);
                vs.setRoomId(requestDto.getRoomId());

                vsList.add(vs);

                if (pictures.size() / 2 <= i + 1) {
                    break;
                }
            }
            room.setState(RoomStatus.PLAYING);
            roomRepository.save(room);

            GameResponseDto responseDto = GameResponseDto.builder()
                .title(majority.get().getTitle())
                .numOfPeople(roomService.getNumOfPeople(requestDto.getRoomId()))
                .maximum(room.getMaximum())
                .totalRound(roundTotal)
                .build();

            vsRepository.saveAll(vsList);

            sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), responseDto);

        } else {
            MajorityMessage message = MajorityMessage.builder()
                .type(SendType.START)
                .message("NOT FOUND DATA").build();
            sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), message);
        }

    }


    @Transactional
    public void vote(VoteRequestDto requestDto) {
        //해당 라운드에서 서로 붙는 picture (공통)
        long roomId = requestDto.getRoomId();
        Room room = roomRepository.findById(roomId).orElseThrow(
            () -> new CustomException(ErrorCode.ROOMS_NOT_FOUND)
        );
        int roundNum = requestDto.getRoundNum();
        long picId1 = requestDto.getPicId1();
        long picId2 = requestDto.getPicId2();

        long winPicId = 0;

        //다수결쪽인 pic 판단
        int sum1 = 0;
        int sum2 = 0;
        List<VoteOptionDto> dtoVoteList = requestDto.getVoteList();
        for (VoteOptionDto vote : dtoVoteList) {
            if (vote.getPickId() == picId1) {
                sum1 += 1;
            } else if (vote.getPickId() == picId2) {
                sum2 += 1;
            } else {
                //아이디가 올바르지 않음
            }
        }
        if (sum1 > sum2) {
            winPicId = picId1;
        } else if (sum2 > sum1) {
            winPicId = picId2;
        } else if (sum1 == sum2) {
            //무승부, 아래로 가지 말고 다시 투표,,
            MajorityMessage message = MajorityMessage.builder()
                .type(SendType.REROUND)
                .message("재투표 필요").build();
            sendingOperations.convertAndSend(PATH + roomId, message);
        }

        //vs레포 저장
        Vs vs = Vs.builder()
            .roomId(roomId)
            .roundNum(roundNum)
            .itemId1(picId1)
            .itemId2(picId2)
            .winner(winPicId)
            .build();
        vsRepository.save(vs);

        //vote레포 저장
        List<Vote> voteList=new ArrayList<>();
        for (VoteOptionDto voteDto : dtoVoteList) {
            Vote vote = Vote.builder()
                .roomId(roomId)
                .memberId(voteDto.getMemberId())
                .pickId(voteDto.getPickId())
                .notPickId(voteDto.getNotPickId())
                .isAns(voteDto.getPickId() == winPicId)
                .build();
            voteList.add(vote);
        }
            voteRepository.saveAll(voteList);

        //결과 전송
        VoteResponseDto voteResponseDto = VoteResponseDto.builder()
            .picId1(picId1)
            .picId2(picId2)
            .winPicId(winPicId)
            .pic1num(sum1)
            .pic2num(sum2)
            .roundNum(roundNum)
            .build();
        sendingOperations.convertAndSend(PATH + roomId, voteResponseDto);

    }


    @Transactional
    public void round(RoundRequestDto requestDto) {

        int round = requestDto.getRoundNum();
        Optional<Vs> vsOptional = vsRepository.findByRoundNumAndRoomId(round,
            requestDto.getRoomId());

        if (vsOptional.isPresent()) {
            Vs vs = vsOptional.get();
            Picture item1 = pictureRepository.findById(vs.getItemId1()).get();
            Picture item2 = pictureRepository.findById(vs.getItemId2()).get();

            RoundResponseDto responseDto = RoundResponseDto
                .builder()
                .item1(item1)
                .item2(item2)
                .roundNum(round)
                .roundTotal(requestDto.getTotalRound())
                .build();

            sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), responseDto);

            if (round < requestDto.getTotalRound() && round % 2 == 0) {
                addVs(round, requestDto.getRoomId());
            }
        }


    }

    public void addVs(int round, Long roomId) {
        Optional<Vs> vsOptional = vsRepository.findByRoundNumAndRoomId(round + 1, roomId);
        Optional<Vs> vsOptional2 = vsRepository.findByRoundNumAndRoomId(round + 2, roomId);

        Vs vs1 = vsOptional.get();
        Vs vs2 = vsOptional2.get();

        Vs vs = new Vs();
        vs.setItemId1(vs1.getWinner());
        vs.setItemId2(vs2.getWinner());
        vs.setRoundNum(round / 2 + 1);
        vs.setRoomId(roomId);

        vsRepository.save(vs);
    }

    @Transactional
    public List<MajorityResponseDto> getAllMajority() {
        List<Majority> majorityList = majorityRepository.findAll();
        return majorityList.stream().map(majority -> MajorityResponseDto.builder()
            .id(majority.getId())
            .title(majority.getTitle())
            .build()).collect(Collectors.toList());
    }

    @Transactional
    public void enter(EnterRequestDto requestDto) {
        Optional<Member> memberOptional = memberRepository.findByNickname(requestDto.getNickname());
        if (memberOptional.isEmpty()) {
            MajorityMessage message = MajorityMessage.builder()
                .type(SendType.ENTER)
                .message("NOT FOUND Member").build();
            sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), message);
            throw new CustomException(ErrorCode.ROOMS_NOT_FOUND);
        }

        ChatResponseDto message = ChatResponseDto
            .builder()
            .sender(requestDto.getNickname())
            .message(requestDto.getNickname() + "님이 입장하였습니다.")
            .roomId(requestDto.getRoomId())
            .build();

        Member member = memberOptional.get();

        addParticipant(requestDto.getNickname(), requestDto.getRoomId());

        sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), EnterResponseDto
            .builder()
            .member(MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .roleType(member.getRoleType())
                .build())
            .build());
        sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), message);
    }

    public void chat(ChatResponseDto message) {
        sendingOperations.convertAndSend(PATH + message.getRoomId(), message);
    }

    public void exit(EnterRequestDto requestDto) {
        Optional<Member> memberOptional = memberRepository.findByNickname(requestDto.getNickname());
        if (memberOptional.isEmpty()) {
            MajorityMessage message = MajorityMessage
                .builder()
                .type(SendType.EXIT)
                .message("NOT FOUND Member").build();
            sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), message);
            throw new CustomException(ErrorCode.ROOMS_NOT_FOUND);
        }

        Member member = memberOptional.get();

        removeParticipant(requestDto.getNickname(), requestDto.getRoomId());

        ChatResponseDto message = ChatResponseDto
            .builder()
            .sender(requestDto.getNickname())
            .message(requestDto.getNickname() + "님이 퇴장하였습니다.")
            .roomId(requestDto.getRoomId())
            .build();

        sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), ExitResponseDto
            .builder()
            .member(MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .roleType(member.getRoleType())
                .build())
            .build());
        sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), message);

    }

    @Transactional
    public void addParticipant(String id, Long roomId) {
        Room room = findRoom(roomId, SendType.ENTER);
        int numOfParticipant = participantRepository.numOfParticipants(roomId);
        if (room.getMaximum() <= numOfParticipant) {
            MajorityMessage message = MajorityMessage
                .builder()
                .message("ALREADY FULL")
                .type(SendType.ENTER)
                .build();
            sendingOperations.convertAndSend(PATH + roomId, message);
            throw new CustomException(ErrorCode.CHECK_FAILED);
        } else if (room.getMaximum() - 1 == numOfParticipant) {
            room.setState(RoomStatus.FULL);
            roomRepository.save(room);
        }

        participantRepository.save(Participant.builder()
            .roomId(roomId)
            .id(id)
            .hostTF(false)
            .build());
    }

    public void removeParticipant(String id, Long roomId) {
        int numOfParticipant = participantRepository.numOfParticipants(roomId);
        if (numOfParticipant == 1) {
            roomRepository.deleteById(roomId);
            participantRepository.deleteById(id);
            return;
        }

        Optional<Participant> participantOptional = participantRepository.findById(id);
        if (participantOptional.isEmpty()) {
            MajorityMessage message = MajorityMessage
                .builder()
                .message("NOT FOUND PARTICIPANT")
                .type(SendType.EXIT)
                .build();
            sendingOperations.convertAndSend(PATH + roomId, message);
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Participant participant = participantOptional.get();
        boolean isHost = participant.isHostTF();
        participantRepository.delete(participant);

        if (isHost) {
            Participant host = participantRepository.findFirstByRoomId(roomId);
            host.setHostTF(true);
            participantRepository.save(host);

            Room room = findRoom(roomId, SendType.EXIT);
            room.setHostId(host.getId());
            roomRepository.save(room);
        }

    }
}
