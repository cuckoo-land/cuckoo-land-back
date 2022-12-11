package com.example.cuckoolandback.ranking.controller;

import com.example.cuckoolandback.ranking.dto.RankingResponseDto;
import com.example.cuckoolandback.ranking.service.RankingService;
import com.example.cuckoolandback.user.service.MemberService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    final MemberService memberService;
    private final RankingService rankingService;

    @ApiOperation(value = "마피아 전체 랭킹 조회")
    @GetMapping("/mafia/total")
    public ResponseEntity<List<RankingResponseDto>> getAllRanking() {
        return ResponseEntity.ok().body(rankingService.getAllMafiaRanking());
    }
}
