package com.example.cuckoolandback.friend.dto;

import com.example.cuckoolandback.friend.domain.FriendStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendResponseDto {
    String friendName;
    FriendStatus friendStatus;
}
