package com.example.cuckoolandback.ranking.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingResponseDto {
    private String memberId;
    private String nickname;
    private int tier;
    private String winNum;
    private int total;
    private int winScore;
    private String memo;
}
