package com.example.cuckoolandback.majority.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteResult {
    private int roundNum;
//    private Long itemId1;
//    private Long itemId2;
    private Long winner;
//    private int numOfVote1;
//    private int numOfVote2;
//    private int numOfTotal;
    private double winnerRate;

}
