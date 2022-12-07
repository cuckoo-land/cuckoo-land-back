package com.example.cuckoolandback.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel(value = "친구요청 및 삭제 객체", description = "친구 추가 및 삭제를 위한 객체")
@Getter
public class FriendRequestDto {
    @ApiModelProperty(value="아이디", example = "bird1", required = true)
    private String friendId;
}
