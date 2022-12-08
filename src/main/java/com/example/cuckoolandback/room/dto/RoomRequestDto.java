package com.example.cuckoolandback.room.dto;


import com.example.cuckoolandback.room.domain.GameType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel(value = "게임 방 생성 객체", description = "게임 방 생성을 위한 객체")
@Getter
public class RoomRequestDto {
    @ApiModelProperty(value="제목", example = "같이 게임 해요", required = true)
    private String title;

    @ApiModelProperty(value="방장", example = "bird1", required = true)
    private String hostId;

    @ApiModelProperty(value="게임종류", example = "0", required = true)
    private GameType type;

    @ApiModelProperty(value="공개유무", example = "true", required = true)
    private boolean visibility;

    @ApiModelProperty(value="최대인원", example = "4", required = true)
    private int maximum;

    @ApiModelProperty(value="비밀번호", example = "password1", required = true)
    private String password;
}
