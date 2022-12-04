package com.example.cuckoolandback.user.jwt;

import com.example.cuckoolandback.common.domain.BaseTime;
import com.example.cuckoolandback.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseTime {
    @Id
    private String memberId;

    private String token;

    public RefreshToken(Member member, String token) {
        this.memberId = member.getMemberId();
        this.token = token;
    }

    public void updateToken(String refreshToken) {
        this.token = refreshToken;
    }
}
