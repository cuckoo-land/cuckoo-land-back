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
    final String PATH = "/topic/majority/";
}
