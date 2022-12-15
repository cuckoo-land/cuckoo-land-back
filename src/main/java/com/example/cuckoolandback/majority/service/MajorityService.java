package com.example.cuckoolandback.majority.service;

import com.example.cuckoolandback.common.exception.CustomException;
import com.example.cuckoolandback.common.exception.ErrorCode;
import com.example.cuckoolandback.majority.domain.Majority;
import com.example.cuckoolandback.majority.domain.Picture;
import com.example.cuckoolandback.majority.domain.Vs;
import com.example.cuckoolandback.majority.dto.*;
import com.example.cuckoolandback.majority.repository.MajorityRepository;
import com.example.cuckoolandback.majority.repository.PictureRepository;
import com.example.cuckoolandback.majority.repository.VsRepository;
import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.room.domain.RoomStatus;
import com.example.cuckoolandback.room.dto.MessageResponseDto;
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
    private final ParticipantRepository participantRepository;
    public Optional<Majority> getRandom() {
        return majorityRepository.findMajorityByRandom();
    }

    final String PATH = "/topic/majority/";

    public Room findRoom(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isEmpty()) {
            MessageResponseDto message = MessageResponseDto.builder().message("NOT FOUND ROOM").build();
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
        Room room = findRoom(requestDto.getRoomId());
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

                if (pictures.size() / 2 <= i + 1) break;
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
            MessageResponseDto message = MessageResponseDto.builder().message("NOT FOUND DATA").build();
            sendingOperations.convertAndSend(PATH + requestDto.getRoomId(), message);
        }

    }

    @Transactional
    public void round(RoundRequestDto requestDto) {

        int round = requestDto.getRoundNum();
        Optional<Vs> vsOptional = vsRepository.findByRoundNumAndRoomId(round, requestDto.getRoomId());

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

    public void chat(ChatResponseDto message) {
        sendingOperations.convertAndSend(PATH + message.getRoomId(), message);
    }
}
