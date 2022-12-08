package com.example.cuckoolandback.room.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
@ApiModel(value = "게임 방 비밀번호 객체", description = "게임 방 비밀번호 체크")
@Getter
public class CheckRoomPasswordRequestDto {
    @ApiModelProperty(value="비밀번호", example = "password1", required = true)
    String password;

    @ApiModelProperty(value="게임방 아이디", example = "0", required = true)
    Long roomId;
}
