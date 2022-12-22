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
public class VoteRequestDto {
    private long roomId;
    private int roundNum;
    //해당 라운드에서 서로 붙는 pic
    private long picId1;
    private long picId2;

    //List로 받기 위한
    private List<VoteOptionDto> voteList;
}
