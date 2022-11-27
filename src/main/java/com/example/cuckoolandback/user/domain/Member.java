package com.example.cuckoolandback.user.domain;

import com.example.cuckoolandback.common.domain.BaseTime;
import lombok.*;
import javax.persistence.*;

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

}
