package com.example.cuckoolandback.majority.dto;

import com.example.cuckoolandback.majority.domain.Picture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoundResponseDto {
    final SendType type = SendType.ROUND;
    Picture item1;
    Picture item2;
    int roundNum;
    int roundTotal;
}
