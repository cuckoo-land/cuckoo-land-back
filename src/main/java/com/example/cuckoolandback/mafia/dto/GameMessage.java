package com.example.cuckoolandback.mafia.dto;

import com.example.cuckoolandback.common.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameMessage {
    private Long roomId;
    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType {
        ENTER, START, LEAVE, DAY, MISSION, FAILMISSION, VOTE, CHAT, NIGHT, PICK, ENDCHECK, SERVER
    }
}
