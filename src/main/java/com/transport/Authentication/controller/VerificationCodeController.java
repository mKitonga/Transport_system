package com.transport.Authentication.controller;

import com.transport.Authentication.Entity.VerificationCodeUse;
import com.transport.Authentication.Service.VerificationCodeService;
import com.transport.Authentication.form.VerificationCodeRequestForm;
import com.transport.Authentication.view.VerificationCodeView;
import com.transport.User.service.BaseUserReadService;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Locale;

public class VerificationCodeController<USR_SRV extends BaseUserReadService<?, ?>> {
    private VerificationCodeService verificationCodeService;
    protected USR_SRV userService;

    @PostMapping("request")
    public EntityApiResponse<VerificationCodeView> requestAuthCode(
            @RequestBody @Valid VerificationCodeRequestForm form,
            Locale locale) {
        return requestVerificationCode(form, locale);
    }

    @PostMapping("resend/{verificationCodeId}")
    public EntityApiResponse<VerificationCodeView> resendVerificationCde(
            @PathVariable String verificationCodeId,
            Locale locale) {

        var verificationCode = verificationCodeService.findByEntityId(verificationCodeId);

        var form = new VerificationCodeRequestForm();
        form.setUsernameType(verificationCode.getUsernameType());
        form.setUsername(verificationCode.getUsername());
        form.setVerificationCodeUse(verificationCode.getVerificationCodeUse());

        return requestVerificationCode(form, locale);
    }

    private EntityApiResponse<VerificationCodeView> requestVerificationCode(VerificationCodeRequestForm form, Locale locale){
        if(form.getVerificationCodeUse() == VerificationCodeUse.AUTHENTICATION){
            checkUserExists(form);
        }

        var verificationCode = verificationCodeService.generateVerificationCode(form);

        String template = switch (form.getUsernameType()) {
            case PHONE_NUMBER -> "verification.code.sent.to.phone";
            case EMAIL -> "verification.code.sent.to.email";
        };

        String msg = String.format(
                Message.get(template, locale),
                form.getUsername()
        );

        return new EntityApiResponse<>(
                msg,
                new VerificationCodeView(verificationCode)
        );
    }

    protected void checkUserExists(VerificationCodeRequestForm form) {
        boolean exists  = switch (form.getUsernameType()) {
            case EMAIL -> userService.existsByEmail(form.getUsername());
            case PHONE_NUMBER -> userService.existsByPhoneNumber(form.getUsername());
        };

        String msgCode = switch (form.getUsernameType()) {
            case EMAIL -> "error.user.email.not.found";
            case PHONE_NUMBER -> "error.user.phone.number.not.found";
        };

        if(!exists){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    msgCode
            );
        }
    }

    @Autowired
    public void setVerificationCodeService(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @Autowired
    public void setUserService(USR_SRV userService) {
        this.userService = userService;
    }
}

