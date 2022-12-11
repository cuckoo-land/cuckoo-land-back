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
                .nickname(member.getNickname())
                .tier(member.getMafiaTier())
                .winNum(member.getMafiaWinNum())
                .winScore(member.getMafiaWinScore())
                .build();
            rankingResponseDtoList.add(rankingResponseDto);
        }
        return rankingResponseDtoList;
    }

    public List<RankingResponseDto> getAllMajorRanking() {
        List<RankingResponseDto> rankingResponseDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findTopByMajorWinScore();
        for (Member member : memberList) {
            RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
                .nickname(member.getNickname())
                .tier(member.getMajorTier())
                .winNum(member.getMajorWinNum())
                .winScore(member.getMajorWinScore())
                .build();
            rankingResponseDtoList.add(rankingResponseDto);
        }
        return rankingResponseDtoList;
    }


}
