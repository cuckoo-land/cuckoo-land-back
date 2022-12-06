package com.example.cuckoolandback.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "닉네임 객체", description = "닉네임 정보를 받기위한 객체")
@Getter
public class NickRequestDto {
    @ApiModelProperty(value="닉네임", example = "뻐꾹이1", required = true)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z-0-9]{4,16}$", message = "닉네임은 특수문자를 제외한 4~16자리여야 합니다.")
    private String nickname;
}
