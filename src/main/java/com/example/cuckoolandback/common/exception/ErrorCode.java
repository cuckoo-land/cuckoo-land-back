package com.example.cuckoolandback.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLE_ID(HttpStatus.BAD_REQUEST, "400", "중복된 아이디 입니다."),
    DUPLE_NICKNAME(HttpStatus.BAD_REQUEST, "400", "중복된 닉네임 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다."),
    CONFIRM_ID_PWD(HttpStatus.BAD_REQUEST, "400", "아이디 또는 비밀번호를 확인해주세요."),
    NOT_LOGIN(HttpStatus.BAD_REQUEST, "400", "로그인이 필요합니다."),
    NOT_EXPIRED_TOKEN_YET(HttpStatus.BAD_REQUEST,"400", "토큰이 만료되지 않았습니다."),
    NEED_NICK(HttpStatus.BAD_REQUEST, "400", "닉네임 입력 후 서비스 이용 가능합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"400","유효하지 않은 리프레시 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

}
