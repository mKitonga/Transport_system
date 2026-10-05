package com.transport.liby.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
    NOT_FOUND(HttpStatus.NOT_FOUND.value()),
    ALREADY_EXISTS(HttpStatus.CONFLICT.value()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value()),
    FORBIDDEN(HttpStatus.FORBIDDEN.value()),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value());
    private final int type;

    ExceptionType(int type) {
        this.type = type;
    }

    public int value(){
        return type;
    }
}
