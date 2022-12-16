package com.example.cuckoolandback.room.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
@ApiModel(value = "메세지 객체", description = "메세지 전달을 위한 객체")
@Getter
@Builder
public class MessageResponseDto {
    @ApiModelProperty(value="메세지", example = "delete success", required = true)
    String message;
}
