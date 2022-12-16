package com.example.cuckoolandback.majority.dto;

import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.dto.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterResponseDto {
    final SendType type = SendType.ENTER;
    MemberResponseDto member;
}
