package com.transport.Authentication.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Jwt {
    private String jwtId;
    private String authToken;
    private LocalDateTime expiryTime;
}
