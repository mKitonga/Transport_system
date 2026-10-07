package com.transport.User.service;

import com.transport.Authentication.Entity.UsernameType;
import com.transport.Authentication.Entity.VerificationCode;
import com.transport.Authentication.Entity.VerificationCodeUse;
import com.transport.Authentication.Service.JwtService;
import com.transport.Authentication.Service.OnUsernameVerificationListener;
import com.transport.Authentication.Service.VerificationCodeService;
import com.transport.Authentication.form.*;
import com.transport.Authentication.view.AuthView;
import com.transport.Authentication.view.Jwt;
import com.transport.User.entity.User;
import com.transport.User.entity.UserStatus;
import com.transport.User.entity.UserType;
import com.transport.User.repository.UserRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public abstract class UserAuthService<U extends User, R extends UserRepository<U>> extends BaseJpaRepoEditService<U, R> {
    private VerificationCodeService verificationCodeService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private List<OnUsernameVerificationListener> usernameVerificationListeners;
    private AuthEventNotificationService authEventNotificationService;

    public abstract UserType getUserType();
    protected abstract U getNewUser();

    public VerificationCode initLogin(InitLoginForm form) {
        U user = resolveUser(form);
        validateUserStatus(user);
        validatePassword(user, form);
        VerificationCodeRequestForm request = new VerificationCodeRequestForm();
        request.setUsername(form.getUsername());
        request.setUsernameType(form.getUsernameType());
        request.setVerificationCodeUse(VerificationCodeUse.AUTHENTICATION);
        return verificationCodeService.generateVerificationCode(request);
    }


    public AuthView completeLogin(CompleteLoginForm form, String verificationCodeId) {
        VerificationCode code = verificationCodeService.findByEntityId(verificationCodeId);
        U user = resolveUser(code);

        verificationCodeService.verifyOtp(verificationCodeId, form.getOtp());
        verificationCodeService.deleteVerificationCode(verificationCodeId);
        if (usernameVerificationListeners != null) {
            usernameVerificationListeners.forEach(
                    listener ->
                            listener.onSuccessfulVerification(
                                    code.getUsernameType(),
                                    user.getEntityId()
                            )
            );
        }
        return onUserAuthentication(user);
    }

    public AuthView register(RegisterForm form) {
        U user = getNewUser();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setUserType(form.getUserType());
        user.setUserStatus(UserStatus.PENDING_APPROVAL);
        user = save(user);
        return onUserAuthentication(user);
    }

    public VerificationCode initPasswordReset(InitLoginForm form) {
        U user = resolveUser(form);
        VerificationCodeRequestForm request = new VerificationCodeRequestForm();
        request.setUsername(form.getUsername());
        request.setUsernameType(form.getUsernameType());
        request.setVerificationCodeUse(VerificationCodeUse.PASSWORD_RESET);
        return verificationCodeService.generateVerificationCode(request);
    }

    public AuthView resetPassword(PasswordResetForm form, String verificationCodeId) {
        VerificationCode code = verificationCodeService.findByEntityId(verificationCodeId);
        verificationCodeService.verifyOtp(verificationCodeId, form.getOtp());
        U user = resolveUser(code);

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(
                    ExceptionType.UNAUTHORIZED,
                    "error.user.not.active"
            );
        }

        user.setPassword(passwordEncoder.encode(form.getPassword()));

        if (code.getUsernameType() == UsernameType.PHONE_NUMBER) {
            user.setPhoneNumberVerified(true);
        } else {
            user.setEmailVerified(true);
        }
        user = save(user);
        verificationCodeService.deleteVerificationCode(verificationCodeId);
        authEventNotificationService.notifyOnPasswordChange(user);
        return onUserAuthentication(user);
    }

    public VerificationCode initEmailVerification(String entityId) {
        U user = findByEntityId(entityId);
        VerificationCodeRequestForm request = new VerificationCodeRequestForm();
        request.setUsername(user.getEmail());
        request.setUsernameType(UsernameType.EMAIL);
        request.setVerificationCodeUse(VerificationCodeUse.EMAIL_VERIFICATION);
        return verificationCodeService.generateVerificationCode(request);
    }


    public void verifyEmail(String entityId, String verificationCodeId) {
        U user = findByEntityId(entityId);
        VerificationCode code = verificationCodeService.findByEntityId(verificationCodeId);
        verificationCodeService.verifyOtp(verificationCodeId, code.getOtp());
        user.setEmailVerified(true);
        save(user);
        verificationCodeService.deleteVerificationCode(verificationCodeId);
    }

    public VerificationCode initPhoneNumberVerification(String entityId) {
        U user = findByEntityId(entityId);
        VerificationCodeRequestForm request = new VerificationCodeRequestForm();
        request.setUsername(user.getPhoneNumber());
        request.setUsernameType(UsernameType.PHONE_NUMBER);
        request.setVerificationCodeUse(VerificationCodeUse.PHONE_NUMBER_VERIFICATION);
        return verificationCodeService.generateVerificationCode(request);
    }

    public void verifyPhoneNumber(String entityId, String verificationCodeId) {
        U user = findByEntityId(entityId);
        VerificationCode code = verificationCodeService.findByEntityId(verificationCodeId);
        verificationCodeService.verifyOtp(verificationCodeId, code.getOtp());
        user.setPhoneNumberVerified(true);
        save(user);
        verificationCodeService.deleteVerificationCode(verificationCodeId);
    }

    public AuthView getCurrentAuthenticatedUser(String entityId) {
        U user = findByEntityId(entityId);
        return onUserAuthentication(user);
    }

    public void logout(String entityId) {
        U user = findByEntityId(entityId);
        user.setRecentAuthId(null);
        save(user);
    }

    public U registerPubKey(PublicKeyForm form, String verificationCodeId) {
        VerificationCode code = verificationCodeService.findByEntityId(verificationCodeId);
        verificationCodeService.verifyOtp(verificationCodeId, form.getOtp());
        verificationCodeService.deleteVerificationCode(verificationCodeId);
        U user = resolveUser(code);
        user.setPublicKey(form.getPublicKey());
        return save(user);
    }

    private U resolveUser(InitLoginForm form) {
        return switch (form.getUsernameType()) {

            case EMAIL -> getUserByEmail(form.getUsername());
            case PHONE_NUMBER -> getUserByPhoneNumber(form.getUsername());
        };
    }

    private U resolveUser(VerificationCode code) {
        return switch (code.getUsernameType()) {
            case EMAIL -> getUserByEmail(code.getUsername());
            case PHONE_NUMBER -> getUserByPhoneNumber(code.getUsername());
        };
    }

    public U getUserByPhoneNumber(String phoneNumber) {
        Specification<U> spec = repository.notDeleted()
                        .and(repository.phoneNumberIs(phoneNumber));
        return repository.findOne(spec)
                .orElseThrow(() ->
                        new CommonRuntimeException(
                                ExceptionType.NOT_FOUND,
                                "error.user.phone.number.not.found"
                        )
                );
    }

    public U getUserByEmail(String email) {
        Specification<U> spec = repository.notDeleted()
                        .and(repository.emailIs(email));
        return repository.findOne(spec)
                .orElseThrow(() ->
                        new CommonRuntimeException(
                                ExceptionType.NOT_FOUND,
                                "error.user.email.not.found"
                        )
                );
    }

    public AuthView onUserAuthentication(U user) {
        Jwt jwt = jwtService.generateJwt(user);
        user.setRecentAuthId(passwordEncoder.encode(jwt.getJwtId()));
        save(user);
        return new AuthView(jwt);
    }

    public void updateNotificationId(NotificationIdUpdateForm form, String userEntityId) {
        U user = findByEntityId(userEntityId);
        user.setNotificationId(form.getNotificationId());
        save(user, form.getSessionUserId());
    }

    public List<String> getUserPermissions(U user) {
        return List.of();
    }


    public void validateUserStatus(U user) {
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.user.not.active"
            );
        }
    }

    private void validatePassword(U user, InitLoginForm form) {
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new CommonRuntimeException(
                    ExceptionType.UNAUTHORIZED,
                    "error.invalid.username.password"
            );
        }
    }

    @Autowired
    public void setVerificationCodeService(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setAuthEventNotificationService(AuthEventNotificationService authEventNotificationService) {
        this.authEventNotificationService = authEventNotificationService;
    }

    @Autowired
    public void setUsernameVerificationListeners(ObjectProvider<OnUsernameVerificationListener> usernameVerificationListeners) {
        this.usernameVerificationListeners = usernameVerificationListeners.orderedStream().toList();
    }
}
