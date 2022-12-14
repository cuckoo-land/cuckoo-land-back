package com.example.cuckoolandback.mafia;

import com.example.cuckoolandback.user.domain.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MafiaUserResponseDto {
    private String memberId;
    private String nickname;
    private RoleType roleType;

    public MafiaUserResponseDto(String memberId, String nickname, RoleType roleType) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.roleType = roleType;
    }
}
