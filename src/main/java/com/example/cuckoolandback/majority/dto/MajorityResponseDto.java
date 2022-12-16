package com.example.cuckoolandback.majority.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "다수결 게임 주제 객체", description = "게임 목록 전달을 위한 객체")
public class MajorityResponseDto {
    @ApiModelProperty(value="게임 주제 아이디", example = "1", required = true)
    Long id;

    @ApiModelProperty(value="게임 주제 제목", example = "제일 귀여운 동물의 숲 캐릭터", required = true)
    String title;
}
