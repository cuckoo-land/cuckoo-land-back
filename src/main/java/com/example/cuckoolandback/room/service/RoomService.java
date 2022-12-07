package com.example.cuckoolandback.room.service;

import com.example.cuckoolandback.common.Message;
import com.example.cuckoolandback.common.exception.CustomException;
import com.example.cuckoolandback.common.exception.ErrorCode;
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
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {

        Room room = Room.builder()
                .code(UUID.randomUUID().toString())
                .title(roomRequestDto.getTitle())
                .hostId(roomRequestDto.getHostId())
                .type(roomRequestDto.getType())
                .state(RoomStatus.WAITING)
                .visibility(roomRequestDto.isVisibility())
                .maximum(roomRequestDto.getMaximum())
                .password(passwordEncoder.encode(roomRequestDto.getPassword()))
                .build();

        roomRepository.save(room);

        participantRepository.save(Participant.builder()
                        .id(roomRequestDto.getHostId())
                        .roomId(room.getId())
                .build());

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
