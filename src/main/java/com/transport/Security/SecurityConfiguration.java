package com.transport.Security;

import com.transport.Authentication.Service.JwtService;
import com.transport.User.entity.UserType;
import com.transport.User.service.UserAuthService;
import com.transport.liby.service.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private AuthenticationExceptionHandler authExceptionHandler;
    private Map<UserType, UserAuthService<?, ?>> userServiceMap = new HashMap<>();
    private JwtService jwtService;

    private final String[] permittedUrls = {
            SystemConfig.STS_USER_BASE_URL + "/auth/register",
            SystemConfig.STS_USER_BASE_URL + "/auth/init-login",
            SystemConfig.STS_USER_BASE_URL + "/auth/complete-login/**",
            SystemConfig.STS_USER_BASE_URL + "/auth/init-password-reset",
            SystemConfig.STS_USER_BASE_URL + "/auth/reset-password/**",
            SystemConfig.STS_USER_BASE_URL + "/verification-code/request/**",
            SystemConfig.STS_USER_BASE_URL + "/verification-code/resend/**",
            SystemConfig.STS_USER_BASE_URL + "/auth/reset-password/**",
            SystemConfig.STS_USER_BASE_URL + "/auth/init-login/**",
            SystemConfig.STS_USER_BASE_URL + "/auth/complete-login/**",
            SystemConfig.STS_USER_BASE_URL + "/auth/update-public-key/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authFilter = new AuthenticationFilter(jwtService, userServiceMap);
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(matcherReg ->
                        matcherReg
                                .requestMatchers(HttpMethod.OPTIONS)
                                .permitAll()
                )
                .authorizeHttpRequests(matcherReg ->
                        matcherReg
                                .requestMatchers(permittedUrls)
                                .permitAll()
                )
                .authorizeHttpRequests(matcherReg ->
                        matcherReg
                                .requestMatchers(
                                        SystemConfig.STS_USER_BASE_URL + "/**"
                                )
                                .authenticated()
                )
                .authorizeHttpRequests(matcherReg ->
                        matcherReg.anyRequest().authenticated()
                )
                .exceptionHandling(expHandler ->
                        expHandler.authenticationEntryPoint(
                                authExceptionHandler
                        )
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .addFilter(authFilter);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public void setAuthExceptionHandler(AuthenticationExceptionHandler authExceptionHandler) {
        this.authExceptionHandler = authExceptionHandler;
    }

    @Autowired
    public void setUserServices(ObjectProvider<UserAuthService<?, ?>> userServices) {
        this.userServiceMap = new HashMap<>();
        userServices.orderedStream().forEach(userService ->
                this.userServiceMap.put(userService.getUserType(), userService));
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
