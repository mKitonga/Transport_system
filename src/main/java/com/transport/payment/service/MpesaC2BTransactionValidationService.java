package com.transport.payment.service;

import com.transport.liby.service.BaseJpaRepoEditService;
import com.transport.payment.entity.MpesaC2BPayment;
import com.transport.payment.entity.MpesaTransactionValidation;
import com.transport.payment.entity.MpesaValidationStatus;
import com.transport.payment.entity.Payment;
import com.transport.payment.repository.MpesaC2BPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MpesaC2BTransactionValidationService extends BaseJpaRepoEditService<MpesaC2BPayment, MpesaC2BPaymentRepository> implements MpesaTransactionValidationListener {

    private List<PaymentListener> paymentListeners;

    @Override
    @Transactional
    public void afterCallback(MpesaTransactionValidation validation) {
        var spec = repository.notDeleted()
                .and(repository.transactionIdIs(validation.getTransactionId()));
        var payment = repository.findOne(spec).orElse(null);

        if (payment == null) {
            return;
        }

        payment.setValidationStatus(validation.getValidationStatus());
        save(payment);

        if (payment.getValidationStatus() == MpesaValidationStatus.CONFIRMED) {
            doOnSuccessfulPayment(payment);
        }
    }

    private void doOnSuccessfulPayment(MpesaC2BPayment c2BPayment) {
        var payment = new Payment();
        payment.setAccountReference(c2BPayment.getTransactionReference());
        payment.setTransactionId(c2BPayment.getTransactionId());
        payment.setAmount(c2BPayment.getTransAmount());

        if (paymentListeners != null) {
            for (PaymentListener listener : paymentListeners) {
                try {
                    listener.onSuccessfulPayment(payment);
                } catch (Exception e) {
                    log.error("Error occurred while processing payment", e);
                }
            }
        }
    }

    @Autowired(required = false)
    public void setPaymentListeners(List<PaymentListener> paymentListeners) {
        this.paymentListeners = paymentListeners;
    }
}
