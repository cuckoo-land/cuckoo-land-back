package com.example.cuckoolandback.majority.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {
    final  SendType type = SendType.CHAT;
    private String sender;
    private Long roomId;
    private String message;
}
