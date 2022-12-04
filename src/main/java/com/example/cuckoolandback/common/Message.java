package com.example.cuckoolandback.common;

public enum Message {
    //MEMBER
    AVAILABLE_NICK("사용 가능한 닉네임 입니다."),
    AVAILABLE_ID("사용 가능한 아이디 입니다."),
    REGISTER_SUCCESS("회원가입 완료 하였습니다."),
    JWT_HEADER_NAME("Authorization"),
    REFRESH_HEADER_NAME("RefreshToken"),
    LOGIN_SUCCESS("로그인 완료"),
    REISSUE_COMPLETED_TOKEN("토큰 재발급 완료"),
    LOGOUT_SUCCESS("로그아웃 완료"),
    AUTHENTICATION_FAIL("Authentication failed, login or reissue token"),

    //FRIEND
    REQUEST_SUCCESS("친구요청 완료"),
    ACCEPT_SUCCESS("친구수락 완료"),
    DELETE_SUCCESS("친구삭제 완료")
    ;

    final private String msg;
    public String getMsg() {
        return msg;
    }
    private Message(String msg){
        this.msg = msg;
    }
}
