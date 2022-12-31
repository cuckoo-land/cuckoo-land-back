package com.example.cuckoolandback.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicRankResponseDto {
    Long majorityId; //주제
    String firstName;
    String secondName;
    String thirdName;
}
