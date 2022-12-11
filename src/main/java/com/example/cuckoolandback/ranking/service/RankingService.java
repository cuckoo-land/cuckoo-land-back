package com.example.cuckoolandback.ranking.service;

import com.example.cuckoolandback.ranking.dto.RankingResponseDto;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;

public class RankingService {

    private final MemberRepository memberRepository;

    public RankingService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<RankingResponseDto> getAllMafiaRanking() {
        List<RankingResponseDto> rankingResponseDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findTopByMafiaWinScore();
        for (Member member : memberList) {
            RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
                .mafiaWinNum(member.getMafiaWinNum())
                .majorWinScore(member.getMajorWinScore())
                .mafiaTier(member.getMafiaTier())
                .majorWinNum(member.getMajorWinNum())
                .majorWinScore(member.getMajorWinScore())
                .majorTier(member.getMajorTier())
                .build();
            rankingResponseDtoList.add(rankingResponseDto);
        }
        return rankingResponseDtoList;
    }
}
