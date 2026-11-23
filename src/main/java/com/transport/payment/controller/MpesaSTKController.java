package com.transport.payment.controller;

import com.transport.liby.view.EntityApiResponse;
import com.transport.payment.exception.PaymentException;
import com.transport.payment.form.MpesaPaymentForm;
import com.transport.payment.service.MpesaSTKPushService;
import jakarta.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/mpesa/stk")
public class MpesaSTKController {

    private MpesaSTKPushService mpesaSTKPushService;

    @PostMapping("/initiate")
    public EntityApiResponse<String> initiateSTKPush(@Valid @RequestBody MpesaPaymentForm form) throws PaymentException {
        mpesaSTKPushService.stkInitialization(form);
        return new EntityApiResponse<>("STK push initiated successfully");
    }

    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestBody JSONObject callback) throws PaymentException {
        mpesaSTKPushService.handleSTKPushCallback(callback);
        return ResponseEntity.ok(Map.of("ResultCode", 0, "ResultDesc", "Success"));
    }

    @Autowired
    public void setMpesaSTKPushService(MpesaSTKPushService mpesaSTKPushService) {
        this.mpesaSTKPushService = mpesaSTKPushService;
    }
}
