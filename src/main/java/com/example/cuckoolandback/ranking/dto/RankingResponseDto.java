package com.example.cuckoolandback.ranking.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingResponseDto {
    private String mafiaWinNum;
    private int mafiaWinScore;
    private String mafiaTier;
    private String majorWinNum;
    private int majorWinScore;
    private String majorTier;
}
