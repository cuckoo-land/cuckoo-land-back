package com.example.cuckoolandback.mafia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MafiaRoomResponseDto {
    private String roomId;
    private String roomName;
    private MafiaUserResponseDto player1;
    private MafiaUserResponseDto player2;
    private MafiaUserResponseDto player3;
    private MafiaUserResponseDto player4;
    private MafiaUserResponseDto player5;
    private MafiaUserResponseDto player6;

}
