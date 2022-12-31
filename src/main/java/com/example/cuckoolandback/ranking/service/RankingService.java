package com.example.cuckoolandback.ranking.service;

import com.example.cuckoolandback.majority.common.exception.CustomException;
import com.example.cuckoolandback.majority.common.exception.ErrorCode;
import com.example.cuckoolandback.majority.domain.Picture;
import com.example.cuckoolandback.majority.repository.PictureRepository;
import com.example.cuckoolandback.ranking.dto.RankingResponseDto;
import com.example.cuckoolandback.ranking.dto.TopicRankResponseDto;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;

    public List<RankingResponseDto> getAllMafiaRanking() {
        List<RankingResponseDto> rankingResponseDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findFirst10ByOrderByMafiaWinScoreDesc();
        for (Member member : memberList) {
            RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .tier(member.getMafiaTier())
                .winNum(member.getMafiaWinNum())
                .total(member.getMafiaTotal())
                .winScore(member.getMafiaWinScore())
                .memo(member.getMemo())
                .build();
            rankingResponseDtoList.add(rankingResponseDto);
        }
        return rankingResponseDtoList;
    }

    public RankingResponseDto getOneMafiaRanking(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .tier(member.getMafiaTier())
            .winNum(member.getMafiaWinNum())
            .total(member.getMafiaTotal())
            .winScore(member.getMafiaWinScore())
            .memo(member.getMemo())
            .build();
        return rankingResponseDto;
    }

    public List<RankingResponseDto> getAllMajorRanking() {
        List<RankingResponseDto> rankingResponseDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findFirst10ByOrderByMajorWinScoreDesc();
        for (Member member : memberList) {
            RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .tier(member.getMajorTier())
                .winNum(member.getMajorWinNum())
                .total(member.getMajorTotal())
                .winScore(member.getMajorWinScore())
                .memo(member.getMemo())
                .build();
            rankingResponseDtoList.add(rankingResponseDto);
        }
        return rankingResponseDtoList;
    }

    public RankingResponseDto getOneMajorRanking(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(
                ErrorCode.USER_NOT_FOUND));
        RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .tier(member.getMajorTier())
            .winNum(member.getMajorWinNum())
            .total(member.getMajorTotal())
            .winScore(member.getMajorWinScore())
            .memo(member.getMemo())
            .build();
        return rankingResponseDto;
    }

    public List<TopicRankResponseDto> getMajorTopicRanking() {
        List<TopicRankResponseDto> responseDtos = new ArrayList<>();
        for (long majorityId = 1; majorityId < pictureRepository.countAllByMajorityId() + 1;
            majorityId++) {
            List<Picture> pictureList = pictureRepository.findPictureByMajorityId(majorityId);
            TopicRankResponseDto responseDto = TopicRankResponseDto.builder()
                .majorityId(majorityId)
                .firstName(pictureList.get(0).getName())
                .secondName(pictureList.get(1).getName())
                .thirdName(pictureList.get(2).getName())
                .build();

            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

}
