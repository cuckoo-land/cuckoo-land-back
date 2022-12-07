package com.example.cuckoolandback.room.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponseDto {
    @ApiModelProperty(value="메세지", example = "delete success", required = true)
    String message;
}
