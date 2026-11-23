package com.transport.payment.service;

import com.transport.payment.entity.MpesaTransactionValidation;

interface MpesaTransactionValidationListener {
    void afterCallback(MpesaTransactionValidation validation);
}
