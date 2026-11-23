package com.transport.payment.service;

import com.transport.payment.entity.Payment;

public interface PaymentListener {
    void onSuccessfulPayment(Payment payment);
}
