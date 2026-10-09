package com.transport.Authentication.controller;

import com.transport.Authentication.Validator.VerificationCodeExists;
import com.transport.Authentication.form.*;
import com.transport.Authentication.view.AuthView;
import com.transport.Authentication.view.VerificationCodeView;
import com.transport.User.entity.User;
import com.transport.User.service.UserAuthService;
import com.transport.liby.service.Message;
import com.transport.liby.view.ApiResponse;
import com.transport.liby.view.EntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/auth")
public abstract class AuthenticationController<U extends User> {
    protected abstract UserAuthService<U, ?> getAuthService();

    @PostMapping("register")
    public EntityApiResponse<AuthView> register(
            @RequestBody @Valid RegisterForm form,
            Locale locale) {
        var authView = getAuthService().register(form);
        return new EntityApiResponse<>(
                Message.get("registration.success", locale),
                authView
        );
    }

    @PostMapping("init-login")
    public EntityApiResponse<VerificationCodeView> initLogin(
            @RequestBody @Valid InitLoginForm form,
            Locale locale) {
        var verificationCode = getAuthService().initLogin(form);
        var template = switch (form.getUsernameType()) {
            case PHONE_NUMBER -> "verification.code.sent.to.phone";
            case EMAIL -> "verification.code.sent.to.email";
        };
        var msg = String.format(Message.get(template, locale), form.getAnonymisedUsername());
        return new EntityApiResponse<>(msg,
                new VerificationCodeView(verificationCode)
        );
    }

    @PostMapping("complete-login/{verificationCodeId}")
    public EntityApiResponse<AuthView> completeLogin(
            @PathVariable
            @VerificationCodeExists(message = "error.entity.not.found")
            String verificationCodeId,
            @RequestBody @Valid CompleteLoginForm form,
            Locale locale) {
        form.setLocale(locale);
        var authView = getAuthService().completeLogin(form, verificationCodeId);
        return new EntityApiResponse<>(
                Message.get("complete.login.success", locale),
                authView
        );
    }

    @PostMapping("logout")
    public ApiResponse logout(Authentication authentication, Locale locale) {
        getAuthService().logout(authentication.getName());
        return new ApiResponse(
                true,
                HttpStatus.OK.value(),
                Message.get("logout.success", locale)
        );
    }

    @PostMapping("init-password-reset")
    public EntityApiResponse<VerificationCodeView> initPasswordReset(
            @RequestBody @Valid InitLoginForm form,
            Locale locale) {
        var verificationCode = getAuthService().initPasswordReset(form);
        var template = switch (form.getUsernameType()) {
            case PHONE_NUMBER -> "verification.code.sent.to.phone";
            case EMAIL -> "verification.code.sent.to.email";
        };
        var msg = String.format(Message.get(template, locale), form.getAnonymisedUsername());
        return new EntityApiResponse<>(msg,
                new VerificationCodeView(verificationCode)
        );
    }

    @PostMapping("reset-password/{verificationCodeId}")
    public EntityApiResponse<AuthView> resetPassword(
            @PathVariable
            @VerificationCodeExists(message = "error.entity.not.found")
            String verificationCodeId,
            @RequestBody @Valid PasswordResetForm form,
            Locale locale) {
        var authView = getAuthService().resetPassword(form, verificationCodeId);
        return new EntityApiResponse<>(
                Message.get("password.reset.success", locale),
                authView
        );
    }

    @PostMapping("verify-email")
    public EntityApiResponse<VerificationCodeView> verifyEmail(Authentication authentication, Locale locale) {
        var verificationCode = getAuthService().initEmailVerification(authentication.getName());
        return new EntityApiResponse<>(
                Message.get("verification.code.sent.to.email", locale),
                new VerificationCodeView(verificationCode)
        );
    }

    @PostMapping("verify-email/{verificationCodeId}")
    public ApiResponse completeEmailVerification(
            @PathVariable
            @VerificationCodeExists(message = "error.entity.not.found")
            String verificationCodeId,
            Authentication authentication,
            Locale locale) {
        getAuthService().verifyEmail(authentication.getName(), verificationCodeId);

        return new ApiResponse(true,
                HttpStatus.OK.value(),
                Message.get("email.verification.success", locale));
    }

    @PostMapping("verify-phone-number")
    public EntityApiResponse<VerificationCodeView> verifyPhoneNumber(Authentication authentication, Locale locale) {
        var verificationCode = getAuthService().initPhoneNumberVerification(authentication.getName());
        return new EntityApiResponse<>(
                Message.get("verification.code.sent.to.phone", locale),
                new VerificationCodeView(verificationCode)
        );
    }

    @PostMapping("verify-phone-number/{verificationCodeId}")
    public ApiResponse completePhoneNumberVerification(
            @PathVariable
            @VerificationCodeExists(message = "error.entity.not.found")
            String verificationCodeId,
            Authentication authentication,
            Locale locale) {
        getAuthService().verifyPhoneNumber(authentication.getName(), verificationCodeId);
        return new ApiResponse(true,
                HttpStatus.OK.value(),
                Message.get("phone.number.verification.success", locale));
    }

    @GetMapping("authenticated_user")
    public EntityApiResponse<AuthView> getCurrentUser(
            Authentication authentication,
            Locale locale) {
        var authView = getAuthService().getCurrentAuthenticatedUser(authentication.getName());
        return new EntityApiResponse<>(
                Message.get("current.user.success", locale),
                authView
        );
    }

    @PostMapping("update-notification-id")
    public ApiResponse updateNotificationId(
            @RequestBody
            @Valid
            NotificationIdUpdateForm form,
            Authentication auth,
            Locale locale){
        form.setCreatedById(auth.getName());
        getAuthService().updateNotificationId(form, auth.getName());
        return new ApiResponse(
                Message.get("notification.id.updated", locale)
        );
    }
}