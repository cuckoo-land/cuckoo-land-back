package com.example.cuckoolandback.mafia;

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
        ENTER, START, DAY, MISSON, VOTE, CHAT, NIGHT, PICK, END, SERVER
    }
}
