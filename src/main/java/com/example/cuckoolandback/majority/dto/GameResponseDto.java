package com.example.cuckoolandback.majority.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDto {
    final SendType type = SendType.START;
    String title;
    int numOfPeople;
    int maximum;
    int totalRound;
}
