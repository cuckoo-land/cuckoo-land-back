package com.example.cuckoolandback.ranking.service;

import com.example.cuckoolandback.common.exception.CustomException;
import com.example.cuckoolandback.common.exception.ErrorCode;
import com.example.cuckoolandback.ranking.dto.RankingResponseDto;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.domain.UserDetailsImpl;
import com.example.cuckoolandback.user.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final MemberRepository memberRepository;

    public List<RankingResponseDto> getAllMafiaRanking() {
        List<RankingResponseDto> rankingResponseDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findFirst10ByOrderByMafiaWinScoreDesc();
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

    public RankingResponseDto getOneMafiaRanking(String memberId)throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
            .nickname(member.getNickname())
            .tier(member.getMafiaTier())
            .winNum(member.getMafiaWinNum())
            .winScore(member.getMafiaWinScore())
            .build();
        return rankingResponseDto;
    }

    public List<RankingResponseDto> getAllMajorRanking() {
        List<RankingResponseDto> rankingResponseDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findFirst10ByOrderByMajorWinScoreDesc();
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
    public RankingResponseDto getOneMajorRanking(String memberId)throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(
                ErrorCode.USER_NOT_FOUND));
        RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
            .nickname(member.getNickname())
            .tier(member.getMajorTier())
            .winNum(member.getMajorWinNum())
            .winScore(member.getMajorWinScore())
            .build();
        return rankingResponseDto;
    }


}
