package com.example.cuckoolandback.room.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {

    MAFIA("Find the Cuckoo", "마피아"),
    MAJORITY("Cuckoo's Pick", "다수결게임");

    private final String code;
    private final String name;
}
