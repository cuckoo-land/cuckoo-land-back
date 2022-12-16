package com.example.cuckoolandback.majority.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorityMessage {
    String message;
    SendType type;
}
