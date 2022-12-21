package com.example.cuckoolandback.majority.dto;

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
    private long userId;
    private long pickId; //투표한 사진 아이디
    private long notPickId; //투표하지 않은 사진 아이디
}
