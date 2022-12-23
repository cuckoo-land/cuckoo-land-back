package com.example.cuckoolandback.majority.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode code;

    public CustomException(ErrorCode code) {
        this.code = code;
    }
}
