package com.example.cuckoolandback.friend.domain;

import com.example.cuckoolandback.common.domain.BaseTime;
import com.example.cuckoolandback.user.domain.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Friend extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "request_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member request;

    @JoinColumn(name = "response_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member response;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    public void update(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
