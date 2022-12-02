package com.example.cuckoolandback.user.dto;

import com.example.cuckoolandback.user.domain.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
    String memberId;
    String nickname;
    RoleType roleType;
}
