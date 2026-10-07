package com.transport.Authentication.view;

import com.transport.Authentication.Entity.VerificationCode;
import com.transport.liby.view.BaseView;

import java.time.LocalDateTime;

public class VerificationCodeView extends BaseView<VerificationCode> {

    public VerificationCodeView(VerificationCode entity) {
        super(entity);
    }

    public LocalDateTime getExpiryTime(){
        return entity.getExpiryTime();
    }
}