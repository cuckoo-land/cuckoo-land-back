package com.example.cuckoolandback.websocket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type;
    private String sender;
    private String roomId;
    private String message;
}
