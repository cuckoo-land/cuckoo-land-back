package com.example.cuckoolandback.majority.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteOptionDto {

    private String memberId;
    private long pickId; //투표한 사진id
    private long notPickId; //투표하지 않은 id
}
