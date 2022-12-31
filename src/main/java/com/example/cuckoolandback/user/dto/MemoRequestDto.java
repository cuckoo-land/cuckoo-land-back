package com.example.cuckoolandback.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(value = "메모 객체", description = "메모 정보를 받기위한 객체")
@Getter
public class MemoRequestDto {
private String memo;
}
