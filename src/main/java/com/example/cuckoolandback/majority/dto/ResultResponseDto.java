package com.example.cuckoolandback.majority.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponseDto {
    //멤버id 순위
    private String first;
    private String second;
    private String third;
    private String last;
    //투표 결과
    private List<VoteResult> voteResultList;

}
