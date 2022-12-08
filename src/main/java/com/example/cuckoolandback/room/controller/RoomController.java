package com.example.cuckoolandback.room.controller;

import com.example.cuckoolandback.room.dto.CheckRoomPasswordRequestDto;
import com.example.cuckoolandback.room.dto.MessageResponseDto;
import com.example.cuckoolandback.room.dto.RoomRequestDto;
import com.example.cuckoolandback.room.dto.RoomResponseDto;
import com.example.cuckoolandback.room.service.RoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @ApiOperation(value = "게임 방 생성")
    @PostMapping("/rooms")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto roomRequestDto){
        return ResponseEntity.ok()
                .body(roomService.createRoom(roomRequestDto));
    }

    @ApiOperation(value = "게임 방 불러오기")
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponseDto>> getAllRoom(@PageableDefault(page = 0, size = 10) Pageable pageable){
        return ResponseEntity.ok()
                .body(roomService.getAllRooms(pageable));
    }

    @ApiOperation(value = "게임 방 삭제")
    @DeleteMapping("/rooms/{roomid}")
    public ResponseEntity<MessageResponseDto> deleteRoom(@PathVariable Long roomid){
        return ResponseEntity.ok()
                .body(roomService.deleteRoom(roomid));
    }

    @ApiOperation(value = "게임 방 검색")
    @GetMapping("/rooms/search/{keyword}")
    public ResponseEntity<List<RoomResponseDto>> searchRoom(@PathVariable String keyword, @PageableDefault(page = 0, size = 10) Pageable pageable){
        return ResponseEntity.ok()
                .body(roomService.searchRoom(keyword,pageable));
    }

    @ApiOperation(value = "참여코드로 게임 방 검색")
    @GetMapping("/rooms/code/{code}")
    public ResponseEntity<RoomResponseDto> searchRoom4Code(@PathVariable String code){
        return ResponseEntity.ok()
                .body(roomService.searchRoom4Code(code));
    }

    @ApiOperation(value = "비공개 게임방 비밀번호 체크")
    @PostMapping("/rooms/pwcheck")
    public ResponseEntity<MessageResponseDto> checkRoomPassword(@RequestBody CheckRoomPasswordRequestDto requestDto){
        return ResponseEntity.ok()
                .body(roomService.checkRoomPassword(requestDto));
    }
}
