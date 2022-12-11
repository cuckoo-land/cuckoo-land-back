package com.example.cuckoolandback.room.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomStatus {

    WAITING(0, "대기중"),
    PLAYING(1, "게임중"),
    FULL(2, "인원초과");

    private final int code;
    private final String name;
}
