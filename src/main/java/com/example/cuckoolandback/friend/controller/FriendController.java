package com.example.cuckoolandback.friend.controller;

import com.example.cuckoolandback.friend.dto.FriendRequestDto;
import com.example.cuckoolandback.friend.dto.FriendResponseDto;
import com.example.cuckoolandback.friend.service.FriendService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/auth/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @ApiOperation(value = "친구 목록 조회")
    @GetMapping()
    public ResponseEntity<List<FriendResponseDto>> getAllFriends(){
        return ResponseEntity.ok().body(friendService.getAllFriends());
    }

    @ApiOperation(value = "친구 추가")
    @PostMapping()
    public ResponseEntity<String> requestFriend(@RequestBody FriendRequestDto friendRequestDto){
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(friendService.requestFriend(friendRequestDto));
    }

    @ApiOperation(value = "친구 삭제")
    @DeleteMapping()
    public ResponseEntity<String> deleteFriend(@RequestBody FriendRequestDto friendRequestDto){
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(friendService.deleteFriend(friendRequestDto));
    }

    // 수락 & 거절 추가 예정
}
