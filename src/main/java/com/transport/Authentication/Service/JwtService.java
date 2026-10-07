package com.transport.Authentication.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.transport.User.entity.User;
import com.transport.User.entity.UserType;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.Authentication.view.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class JwtService {
    private String jwtKeyStsUser;
    private Integer jwtValidityMins;
    private BCryptPasswordEncoder passwordEncoder;

    public <U extends User> Jwt generateJwt(U user) {
        String entityId = user.getEntityId();
        String jwtId = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(jwtValidityMins);
        String jwtToken = JWT.create()
                .withSubject(entityId)
                .withExpiresAt(expiryTime.toInstant(ZoneOffset.UTC))
                .withJWTId(jwtId)
                .sign(Algorithm.HMAC512(getJwtSecret(user.getUserType()).getBytes()));

        Jwt jwt = new Jwt();
        jwt.setJwtId(jwtId);
        jwt.setAuthToken(jwtToken);
        jwt.setExpiryTime(expiryTime);
        return jwt;
    }

    public DecodedJWT getDecodedJWT(String jwt, UserType userType) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(getJwtSecret(userType).getBytes()))
                .build()
                .verify(jwt.trim());
    }

    public void validateJwtId(String tokenJwtId, String savedJwtId) {
        if (!passwordEncoder.matches(tokenJwtId, savedJwtId)) {
            throw new CommonRuntimeException(
                    ExceptionType.FORBIDDEN,
                    "error.invalid.auth"
            );
        }
    }

    private String getJwtSecret(UserType userType) {
        return jwtKeyStsUser;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {this.passwordEncoder = passwordEncoder;}

    @Value("${jwt.key.sts.user}")
    public void setJwtKeyInternalUser(String jwtKeyInternalUser) {this.jwtKeyStsUser = jwtKeyInternalUser;}

    @Value("${jwt.validity.mins}")
    public void setJwtValidityMins(Integer jwtValidityMins) {this.jwtValidityMins = jwtValidityMins;}
}
