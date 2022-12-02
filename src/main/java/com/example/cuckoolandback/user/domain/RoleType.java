package com.example.cuckoolandback.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    GUEST("ROLE_GUEST", "게스트");

    private final String code;
    private final String displayName;

}
