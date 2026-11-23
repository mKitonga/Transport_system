package com.transport.payment.controller;

import com.transport.liby.view.EntityApiResponse;
import com.transport.payment.exception.PaymentException;
import com.transport.payment.form.MpesaC2BPaymentForm;
import com.transport.payment.service.MpesaC2BPaymentRegistrationService;
import com.transport.payment.service.MpesaTransactionValidationService;
import jakarta.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/mpesa/c2b")
public class MpesaC2BController {

    private MpesaC2BPaymentRegistrationService mpesaC2BPaymentRegistrationService;
    private MpesaTransactionValidationService mpesaTransactionValidationService;

    @PostMapping("/register")
    public EntityApiResponse<String> registerPayment(@Valid @RequestBody MpesaC2BPaymentForm form) {
        mpesaC2BPaymentRegistrationService.registerPayment(form);
        return new EntityApiResponse<>("Payment registered successfully");
    }

    @PostMapping("/validation/callback")
    public ResponseEntity<Map<String, Object>> handleValidationCallback(@RequestBody JSONObject callback) throws PaymentException {
        mpesaTransactionValidationService.transactionStatusCallback(callback);
        return ResponseEntity.ok(Map.of("ResultCode", 0, "ResultDesc", "Success"));
    }

    @PostMapping("/validation/timeout")
    public ResponseEntity<Map<String, Object>> handleValidationTimeout(@RequestBody JSONObject callback) throws PaymentException {
        mpesaTransactionValidationService.transactionStatusTimeout(callback);
        return ResponseEntity.ok(Map.of("ResultCode", 0, "ResultDesc", "Success"));
    }

    @Autowired
    public void setMpesaC2BPaymentRegistrationService(MpesaC2BPaymentRegistrationService mpesaC2BPaymentRegistrationService) {
        this.mpesaC2BPaymentRegistrationService = mpesaC2BPaymentRegistrationService;
    }

    @Autowired
    public void setMpesaTransactionValidationService(MpesaTransactionValidationService mpesaTransactionValidationService) {
        this.mpesaTransactionValidationService = mpesaTransactionValidationService;
    }
}
