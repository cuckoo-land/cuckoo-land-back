package com.example.cuckoolandback.room.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {

    MAFIA("MAFIA", "마피아");
    private final String code;
    private final String name;
}
