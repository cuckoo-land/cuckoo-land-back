package com.example.cuckoolandback.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "로그인 객체", description = "로그인을 하기 위한 객체")
public class LoginRequestDto {

    @ApiModelProperty(value="아이디", example = "user01", required = true)
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String memberId;

    @ApiModelProperty(value="비밀번호", example = "Abcd123!", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
