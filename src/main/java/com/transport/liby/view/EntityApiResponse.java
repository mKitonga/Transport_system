package com.transport.liby.view;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EntityApiResponse<T> extends ApiResponse {
    private T data;

    public EntityApiResponse() {}

    public EntityApiResponse(T data){
        super(true, HttpStatus.OK.value(), "");
        setData(data);
    }

    public EntityApiResponse(String message, T data){
        super(true, HttpStatus.OK.value(), message);
        setData(data);
    }

    public EntityApiResponse(boolean status, int statusCode, String message, T data) {
        super(status, statusCode, message);
        setData(data);
    }
}
