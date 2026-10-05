package com.transport.liby.exception;

import lombok.Getter;

public class CommonRuntimeException extends RuntimeException {
    @Getter
    private final ExceptionType type;
    private final String msg;

    public CommonRuntimeException(ExceptionType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
