package com.example.cuckoolandback.user.domain;

import com.example.cuckoolandback.common.domain.BaseTime;
import com.example.cuckoolandback.friend.domain.Friend;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String memberId;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToMany(mappedBy = "response", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friendList;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
