package com.transport.Authentication.view;

import java.time.LocalDateTime;

public class AuthView {
    private final Jwt entity;
    public  AuthView(Jwt entity){
        this.entity = entity;
    }
    public String getAuthToken(){
        return entity.getAuthToken();
    }
    public LocalDateTime getExpiryTime(){
        return entity.getExpiryTime();
    }
}
