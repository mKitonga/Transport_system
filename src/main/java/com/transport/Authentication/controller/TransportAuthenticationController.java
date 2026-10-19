package com.transport.Authentication.controller;

import com.transport.User.entity.User;
import com.transport.User.service.TransportUserAuthService;
import com.transport.User.service.UserRegistrationService;
import com.transport.User.service.UserAuthService;
import com.transport.Authentication.form.RegisterParentForm;
import com.transport.Authentication.form.RegisterSaccoAdminForm;
import com.transport.Authentication.form.RegisterSaccoDriverForm;
import com.transport.Authentication.form.RegisterSchoolAdminForm;
import com.transport.Authentication.form.RegisterSchoolDriverForm;
import com.transport.Authentication.view.AuthView;
import com.transport.liby.service.SystemConfig;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping(SystemConfig.STS_USER_BASE_URL + "/auth")
public class TransportAuthenticationController extends AuthenticationController<User> {
    private final TransportUserAuthService authService;
    private final UserRegistrationService userRegistrationService;

    public TransportAuthenticationController(
            TransportUserAuthService authService,
            UserRegistrationService userRegistrationService) {
        this.authService = authService;
        this.userRegistrationService = userRegistrationService;
    }

    @Override
    protected UserAuthService<User, ?> getAuthService() {
        return authService;
    }

    @PostMapping("register/parent")
    public EntityApiResponse<AuthView> registerParent(
            @RequestBody @Valid RegisterParentForm form,
            Locale locale) {
        return registrationResponse(userRegistrationService.registerParent(form), locale);
    }

    @PostMapping("register/school-admin")
    public EntityApiResponse<AuthView> registerSchoolAdmin(
            @RequestBody @Valid RegisterSchoolAdminForm form,
            Locale locale) {
        return registrationResponse(userRegistrationService.registerSchoolAdmin(form), locale);
    }

    @PostMapping("register/school-driver")
    public EntityApiResponse<AuthView> registerSchoolDriver(
            @RequestBody @Valid RegisterSchoolDriverForm form,
            Locale locale) {
        return registrationResponse(userRegistrationService.registerSchoolDriver(form), locale);
    }

    @PostMapping("register/sacco-admin")
    public EntityApiResponse<AuthView> registerSaccoAdmin(
            @RequestBody @Valid RegisterSaccoAdminForm form,
            Locale locale) {
        return registrationResponse(userRegistrationService.registerSaccoAdmin(form), locale);
    }

    @PostMapping("register/sacco-driver")
    public EntityApiResponse<AuthView> registerSaccoDriver(
            @RequestBody @Valid RegisterSaccoDriverForm form,
            Locale locale) {
        return registrationResponse(userRegistrationService.registerSaccoDriver(form), locale);
    }

    private EntityApiResponse<AuthView> registrationResponse(AuthView authView, Locale locale) {
        return new EntityApiResponse<>(Message.get("registration.success", locale), authView);
    }
}
