package com.transport.payment.service;

import com.transport.fares.entity.FarePayment;
import com.transport.fares.repository.FarePaymentRepository;
import com.transport.fares.repository.FareRepository;
import com.transport.payment.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FarePaymentListener implements PaymentListener {

    private FarePaymentRepository farePaymentRepository;
    private FareRepository fareRepository;

    @Override
    public void onSuccessfulPayment(Payment payment) {
        if (payment == null || payment.getTransactionId() == null) {
            return;
        }

        try {
            var existing = farePaymentRepository.findByTransactionId(payment.getTransactionId());
            if (existing.isPresent()) {
                return;
            }

            var farePayment = new FarePayment();
            farePayment.setTransactionId(payment.getTransactionId());
            farePayment.setFareAmount(payment.getAmount());

            if (payment.getAccountReference() != null) {
                var fareOpt = fareRepository.findByEntityIdAndDeletedAtIsNull(payment.getAccountReference());
                fareOpt.ifPresent(farePayment::setFare);
            }

            farePaymentRepository.save(farePayment);
            log.info("Fare payment recorded for transaction: {}", payment.getTransactionId());
        } catch (Exception e) {
            log.error("Error saving fare payment for transaction {}", payment.getTransactionId(), e);
        }
    }

    @Autowired
    public void setFarePaymentRepository(FarePaymentRepository farePaymentRepository) {
        this.farePaymentRepository = farePaymentRepository;
    }

    @Autowired
    public void setFareRepository(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }
}
