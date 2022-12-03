package com.example.cuckoolandback.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "아이디 객체", description = "아이디 정보를 받기위한 객체")
@Getter
public class IdRequestDto {
    @ApiModelProperty(value="아이디", example = "bird1", required = true)
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z-0-9]{4,16}$", message = "아이디는 4~16자리 영문, 숫자를 사용합니다.")
    private String memberId;
}
