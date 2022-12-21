package com.example.cuckoolandback.majority.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponseDto {
    final SendType type = SendType.VOTE;
    String pic1;
    String pic2;
    int pic1num;
    int pic2num;
    int roundNum
}
