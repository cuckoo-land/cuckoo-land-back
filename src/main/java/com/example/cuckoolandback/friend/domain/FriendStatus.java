package com.example.cuckoolandback.friend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendStatus {

    REQUESTED("REQUESTED", "수락 대기중"),
    ACCEPTED("ACCEPTED", "완료");

    private final String code;
    private final String displayName;

}
