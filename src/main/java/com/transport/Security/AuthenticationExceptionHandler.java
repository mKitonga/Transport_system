package com.transport.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.liby.service.Message;
import com.transport.liby.view.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public AuthenticationExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authEx)
            throws IOException {

        String languageCode = req.getHeader("Accept-Language");
        if(languageCode == null){
            languageCode = "en";
        }

        Locale locale = new Locale(languageCode);
        String msg;
        int statusCode = res.getStatus();

        switch (statusCode){
            case HttpServletResponse.SC_UNAUTHORIZED:
                msg = Message.get("error.invalid.auth", locale);
                break;
            case HttpServletResponse.SC_FORBIDDEN:
                msg = Message.get("error.operation.not.allowed", locale);
                break;
            case HttpServletResponse.SC_NOT_FOUND:
                msg = Message.get("error.entity.not.found", locale);
                break;
            case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                msg = Message.get("error.method.not.allowed", locale);
                break;
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                msg = Message.get("error.internal.server.error", locale);
                break;
            default:
                msg = authEx.getLocalizedMessage();
                statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        }
        ApiResponse apiResponse = new ApiResponse(
                false,
                statusCode,
                msg
        );
        apiResponse.setTime(LocalDateTime.now());
        String responseMsg = objectMapper.writeValueAsString(apiResponse);

        res.setStatus(statusCode);
        res.setContentType("application/json");
        res.getWriter().write(responseMsg);
    }

}
