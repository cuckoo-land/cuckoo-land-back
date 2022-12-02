package com.example.cuckoolandback.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "회원가입 객체", description = "회원가입을 하기 위한 객체")
@Getter
public class RegisterRequestDto {

    @ApiModelProperty(value="아이디", example = "user01", required = true)
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z-0-9]{4,16}$", message = "아이디는 4~16자리 영문,숫자를 사용합니다.")
    private String memberId;

    @ApiModelProperty(value="닉네임", example = "뻐꾹이", required = true)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z-0-9]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    private String nickname;

    @ApiModelProperty(value="비밀번호", example = "Abcd123", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,32}", message = "비밀번호는 8~32자 영문,숫자,기호 사용하세요.")
    private String password;

}