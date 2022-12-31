package com.example.cuckoolandback.majority.controller;

import com.example.cuckoolandback.majority.dto.*;
import com.example.cuckoolandback.majority.service.MajorityService;
import com.example.cuckoolandback.ranking.dto.RankingResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MajorityController {


    private final MajorityService majorityService;

    //방 입장
    @MessageMapping("/majority/enter")
    public void enter(EnterRequestDto requestDto) {
        majorityService.enter(requestDto);
    }

    //방 정보로 주제선택 후 해당 주제에 관련된 사진 나눠서 DB에 저장
    @MessageMapping("/majority/start")
    public void start(MajorityRequestDto requestDto) {
        majorityService.start(requestDto);
    }

    //방 정보와 몇번째 투표인지에 대한 정보로 이번 투표 사진정보 보내기
    @MessageMapping("/majority/round")
    public void round(RoundRequestDto requestDto) {
        majorityService.round(requestDto);
    }

    //라운드가 끝날 때마다 투표 정보 보내기
    @MessageMapping("/majority/vote")
    public void round(VoteRequestDto requestDto) {
        System.out.println(00000);
        majorityService.vote(requestDto);
    }

    //방 나가기
    @MessageMapping("/majority/exit")
    public void start(EnterRequestDto requestDto) {
        majorityService.exit(requestDto);
    }

    //채팅
    @MessageMapping("/majority/chat")
    public void chat(ChatResponseDto message) {
        majorityService.chat(message);
    }

    @ApiOperation(value = "다수결 게임 목록 불러오기")
    @GetMapping("/api/majority")
    public ResponseEntity<List<MajorityResponseDto>> getAllMajority() {
        return ResponseEntity.ok()
                .body(majorityService.getAllMajority());
    }

    //다수결 결과 조회 (+레포에 결과 반영)
    @GetMapping("/api/majority/result/{roomid}")
    public ResponseEntity<ResultResponseDto> getAllResult(@PathVariable Long roomid) {
        return ResponseEntity.ok().body(majorityService.getAllResult(roomid));
    }

}
