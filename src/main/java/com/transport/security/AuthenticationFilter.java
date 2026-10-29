package com.transport.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.transport.authentication.Service.JwtService;
import com.transport.user.entity.User;
import com.transport.user.entity.UserStatus;
import com.transport.user.entity.UserType;
import com.transport.user.service.UserAuthService;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.SystemConfig;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

class AuthenticationFilter<U extends User> extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtService jwtService;
    private final Map<UserType, UserAuthService<U, ?>> userServiceMap;

    AuthenticationFilter(JwtService jwtService, Map<UserType, UserAuthService<U, ?>> userServiceMap) {
        this.jwtService = jwtService;
        this.userServiceMap = userServiceMap;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (isPublicAuthenticationRequest(req)) {
            chain.doFilter(req, res);
            return;
        }

        String authToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        UserType userType = getUserType(req.getServletPath());
        if (StringUtils.isNotBlank(authToken)) {
            try {
                if (userType == null) {
                    userType = resolveUserType(authToken);
                }
                if (userType == null) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                var auth = authenticateWithBearerToken(userType, authToken);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            } catch (JWTVerificationException | CommonRuntimeException exception) {
                log.warn("Authentication failed for {}: {}", req.getServletPath(), exception.getMessage());
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(req, res);
    }

    private boolean isPublicAuthenticationRequest(HttpServletRequest req) {
        return isPublicPath(req.getServletPath()) || isPublicPath(req.getRequestURI());
    }

    private boolean isPublicPath(String path) {
        if (path == null) {
            return false;
        }
        String authPath = SystemConfig.STS_USER_BASE_URL + "/auth";
        return path.equals("/error")
                || path.startsWith("/error/")
                || path.equals(authPath + "/register")
                || path.startsWith(authPath + "/register/")
                || path.equals(authPath + "/init-login")
                || path.startsWith(authPath + "/init-login/")
                || path.startsWith(authPath + "/complete-login/")
                || path.equals(authPath + "/init-password-reset")
                || path.startsWith(authPath + "/init-password-reset/")
                || path.startsWith(authPath + "/reset-password/")
                || path.startsWith(SystemConfig.STS_USER_BASE_URL + "/verification-code/");
    }

    private UsernamePasswordAuthenticationToken authenticateWithBearerToken(UserType userType, String authToken) {
        U user;
        if (authToken.regionMatches(
                true,
                0,
                "Signature",
                0,
                "Signature".length()
        )) {
            user = validateSignature(authToken, userType);
        } else {
            user = authenticateBearerToken(authToken, userType);
        }

        if (user.getUserType() != userType) {
            throw new CommonRuntimeException(ExceptionType.UNAUTHORIZED, "error.invalid.auth");
        }

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(
                    ExceptionType.UNAUTHORIZED,
                    "error.user.not.active"
            );
        }

        UserAuthService<U, ?> userAuthService = getUserAuthService(userType);
        List<String> authorities = new ArrayList<>(userAuthService.getUserPermissions(user));
        authorities.add(userType.toString());
        return new UsernamePasswordAuthenticationToken(
                user.getEntityId(),
                null,
                authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }

    private U authenticateBearerToken(String authToken, UserType userType) {
        DecodedJWT decodedJWT = jwtService.getDecodedJWT(getTokenValue(authToken), userType);
        UserAuthService<U, ?> userAuthService = getUserAuthService(userType);
        U user = userAuthService.findByEntityId(decodedJWT.getSubject());
        jwtService.validateJwtId(decodedJWT.getId(), user.getRecentAuthId());
        return user;
    }

    private U validateSignature(String authToken, UserType userType) {
        String token = authToken.substring("Signature ".length()).trim();
        String[] tokenParts = token.split(":");
        if (tokenParts.length != 2) {
            throw new CommonRuntimeException(
                    ExceptionType.FORBIDDEN,
                    "error.invalid.auth"
            );
        }

        String userId = tokenParts[0];
        String signature = tokenParts[1];

        UserAuthService<U, ?> userAuthService = getUserAuthService(userType);
        U user = userAuthService.findByEntityId(userId);
        try {
            decrypt(signature, user.getPublicKey());
        } catch (Exception e) {
            throw new CommonRuntimeException(
                    ExceptionType.FORBIDDEN,
                    "error.invalid.auth"
            );
        }
        return user;
    }

    private void decrypt(String encryptedText, String publicKeyStr) throws Exception {
        PublicKey publicKey = loadPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        cipher.doFinal(Base64.getDecoder().decode(encryptedText));
    }

    private PublicKey loadPublicKey(String keyBase64Str) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64Str);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory
                .getInstance("RSA")
                .generatePublic(spec);
    }

    private UserType getUserType(String servletPath) {
        if (servletPath.contains("/sacco-admin")) {
            return UserType.SACCO_ADMIN;
        }

        if (servletPath.contains("/sacco-driver")) {
            return UserType.SACCO_DRIVER;
        }

        if (servletPath.contains("/school-admin")) {
            return UserType.SCHOOL_ADMIN;
        }

        if (servletPath.contains("/school-driver")) {
            return UserType.SCHOOL_DRIVER;
        }

        if (servletPath.contains("/parent")) {
            return UserType.PARENT;
        }
        return null;
    }

    private UserType resolveUserType(String authToken) {
        if (authToken.regionMatches(true, 0, "Signature", 0, "Signature".length())) {
            String token = authToken.substring("Signature ".length()).trim();
            String[] tokenParts = token.split(":");
            if (tokenParts.length != 2) {
                throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.invalid.auth");
            }
            return resolveUserTypeByEntityId(tokenParts[0]);
        }

        String token = getTokenValue(authToken);
        for (UserType candidate : UserType.values()) {
            try {
                DecodedJWT decodedJWT = jwtService.getDecodedJWT(token, candidate);
                String userTypeClaim = decodedJWT.getClaim("userType").asString();
                if (StringUtils.isNotBlank(userTypeClaim)) {
                    try {
                        return UserType.valueOf(userTypeClaim);
                    } catch (IllegalArgumentException ignored) {
                        throw new CommonRuntimeException(ExceptionType.UNAUTHORIZED, "error.invalid.auth");
                    }
                }
                return resolveUserTypeByEntityId(decodedJWT.getSubject());
            } catch (JWTVerificationException ignored) {
                // Try the next configured user type.
            }
        }
        throw new CommonRuntimeException(ExceptionType.UNAUTHORIZED, "error.invalid.auth");
    }

    private UserType resolveUserTypeByEntityId(String userId) {
        for (UserType candidate : UserType.values()) {
            UserAuthService<U, ?> userAuthService = userServiceMap.get(candidate);
            if (userAuthService == null) {
                continue;
            }
            try {
                U user = userAuthService.findByEntityId(userId);
                if (user.getUserType() == candidate) {
                    return candidate;
                }
            } catch (CommonRuntimeException ignored) {
            }
        }
        throw new CommonRuntimeException(ExceptionType.UNAUTHORIZED, "error.invalid.auth");
    }

    private UserAuthService<U, ?> getUserAuthService(UserType userType) {
        UserAuthService<U, ?> userAuthService = userServiceMap.get(userType);
        if (userAuthService == null) {
            userAuthService = userServiceMap.get(UserType.PARENT);
        }
        if (userAuthService == null) {
            throw new CommonRuntimeException(ExceptionType.UNAUTHORIZED, "error.invalid.auth");
        }
        return userAuthService;
    }

    private String getTokenValue(String authToken) {
        if (authToken.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
            return authToken.substring("Bearer ".length()).trim();
        }
        return authToken.trim();
    }
}
