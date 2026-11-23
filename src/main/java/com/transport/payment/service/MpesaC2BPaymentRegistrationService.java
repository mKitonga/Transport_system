package com.transport.payment.service;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import com.transport.payment.entity.MpesaC2BPayment;
import com.transport.payment.exception.PaymentException;
import com.transport.payment.form.MpesaC2BPaymentForm;
import com.transport.payment.repository.MpesaC2BPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class MpesaC2BPaymentRegistrationService extends BaseJpaRepoEditService<MpesaC2BPayment, MpesaC2BPaymentRepository> {

    private MpesaTransactionValidationService mpesaTransactionValidationService;

    public void registerPayment(MpesaC2BPaymentForm form) {

        var spec = repository
                .notDeleted()
                .and(repository.transactionIdIs(form.getTransactionId()));
        boolean exists = repository.exists(spec);

        if (exists) {
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.entity.already.exists"
            );
        }

        var transaction = new MpesaC2BPayment();
        transaction.setTransactionId(form.getTransactionId());
        transaction.setTransactionType(form.getTransactionType());
        transaction.setTransAmount(form.getAmount());
        transaction.setBusinessShortCode(form.getBusinessShortCode());
        transaction.setTransactionReference(form.getTransactionReference());
        transaction.setAccountBalance(form.getAccountBalance());
        transaction.setPhoneNumber(form.getPhoneNumber());
        transaction.setPayerName(form.getPayorName());

        try {
            var mpesaDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            var transactionTime = LocalDateTime.parse(form.getTransactionTime(), mpesaDateTimeFormatter);
            transaction.setTransactionTime(transactionTime);
        } catch (Exception e) {
            log.error("Error logging Mpesa C2B transaction time {}", form.getTransactionTime());
        }

        save(transaction);

        try {
            mpesaTransactionValidationService.queryTransactionStatus(transaction.getTransactionId());
        } catch (PaymentException e) {
            log.error("Error initializing payment validation\n{}", e.getMessage());
        }
    }

    @Autowired
    public void setMpesaTransactionValidationService(MpesaTransactionValidationService mpesaTransactionValidationService) {
        this.mpesaTransactionValidationService = mpesaTransactionValidationService;
    }
}
