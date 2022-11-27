package com.example.cuckoolandback.user.dto;

import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.jwt.TokenDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuestResponseDto {
    TokenDto tokenDto;
    String memberId;
    String nickname;
    String password;
}
