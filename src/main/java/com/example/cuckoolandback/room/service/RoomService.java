package com.example.cuckoolandback.room.service;

import com.example.cuckoolandback.majority.common.Message;
import com.example.cuckoolandback.majority.common.exception.CustomException;
import com.example.cuckoolandback.majority.common.exception.ErrorCode;
import com.example.cuckoolandback.room.domain.Participant;
import com.example.cuckoolandback.room.domain.Room;
import com.example.cuckoolandback.room.domain.RoomStatus;
import com.example.cuckoolandback.room.dto.CheckRoomPasswordRequestDto;
import com.example.cuckoolandback.room.dto.MessageResponseDto;
import com.example.cuckoolandback.room.dto.RoomRequestDto;
import com.example.cuckoolandback.room.dto.RoomResponseDto;
import com.example.cuckoolandback.room.repository.ParticipantRepository;
import com.example.cuckoolandback.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Page<RoomResponseDto> getAllRooms(Pageable pageable) {

        Page<Room> roomList = roomRepository.findAll(pageable);

        List<RoomResponseDto> responseDtoList = roomList.stream().map(room ->  RoomResponseDto.builder()
                        .id(room.getId())
                        .title(room.getTitle())
                        .state(room.getState())
                        .code(room.getCode())
                        .hostId(room.getHostId())
                        .maximum(room.getMaximum())
                        .visibility(room.isVisibility())
                        .type(room.getType())
                        .numOfPeople(getNumOfPeople(room.getId()))
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(responseDtoList,pageable,roomList.getTotalElements());
    }

    public int getNumOfPeople(Long roomId) {
        return participantRepository.numOfParticipants(roomId);
    }

    @Transactional
    public MessageResponseDto checkRoomPassword(CheckRoomPasswordRequestDto requestDto) {
        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.ROOMS_NOT_FOUND));


        // 비밀번호 체크
        if (!passwordEncoder.matches(requestDto.getPassword(), room.getPassword())) {
            throw new CustomException(ErrorCode.CHECK_FAILED);
        }
        return MessageResponseDto.builder()
                .message(Message.PASSWORD_CORRECT.getMsg())
                .build();
    }

    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto){
        Room room = Room.builder()
                .code(UUID.randomUUID().toString().substring(0,8))
                .title(roomRequestDto.getTitle())
                .hostId(roomRequestDto.getHostId())
                .type(roomRequestDto.getType())
                .state(RoomStatus.WAITING)
                .visibility(roomRequestDto.isVisibility())
                .maximum(roomRequestDto.getMaximum())
                .password(passwordEncoder.encode(roomRequestDto.getPassword()))
                .build();

        if(roomRepository.findRoomByCode(room.getCode()).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_CODE);
        }

        try{
            roomRepository.save(room);
            participantRepository.save(Participant.builder()
                    .id(roomRequestDto.getHostId())
                    .roomId(room.getId())
                    .hostTF(true)
                    .build());
        }catch (Exception e){
            throw new CustomException(ErrorCode.CREATE_FAILED);
        }


        return RoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .state(room.getState())
                .code(room.getCode())
                .hostId(room.getHostId())
                .maximum(room.getMaximum())
                .visibility(room.isVisibility())
                .numOfPeople(getNumOfPeople(room.getId()))
                .type(room.getType())
                .build();
    }

    @Transactional
    public MessageResponseDto deleteRoom(Long roomid) {

        Room room = roomRepository.findById(roomid)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOMS_NOT_FOUND));

        roomRepository.delete(room);

        return MessageResponseDto
                .builder()
                .message(Message.DELETE_SUCCESS.getMsg())
                .build();
    }

    @Transactional
    public List<RoomResponseDto> searchRoom(String keyword, Pageable pageable) {

        List<Room> roomList = roomRepository.searchRooms(keyword, pageable);

        List<RoomResponseDto> responseDtoList = roomList.stream().map(room -> RoomResponseDto.builder()
                        .id(room.getId())
                        .title(room.getTitle())
                        .state(room.getState())
                        .code(room.getCode())
                        .hostId(room.getHostId())
                        .numOfPeople(getNumOfPeople(room.getId()))
                        .maximum(room.getMaximum())
                        .visibility(room.isVisibility())
                        .type(room.getType())
                        .build())
                .collect(Collectors.toList());
        return responseDtoList;
    }

    @Transactional
    public RoomResponseDto searchRoom4Code(String code) {

        Room room = roomRepository.findRoomByCode(code)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOMS_NOT_FOUND));

        return RoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .state(room.getState())
                .code(room.getCode())
                .hostId(room.getHostId())
                .maximum(room.getMaximum())
                .visibility(room.isVisibility())
                .numOfPeople(getNumOfPeople(room.getId()))
                .type(room.getType())
                .build();
    }
}
