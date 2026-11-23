package com.transport.payment.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class MpesaSTKPushInitForm {

    @JsonProperty("BusinessShortCode")
    private String primaryBusinessShortCode;

    @JsonProperty("PartyB")
    private String childBusinessShortCode;

    @JsonProperty("PartyA")
    private String payorPhoneNumber;

    @JsonProperty("Amount")
    private BigDecimal amount;

    public BigDecimal getFareAmount() {
        return amount;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.amount = fareAmount;
    }

    @JsonProperty("AccountReference")
    private String accountReference;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("TransactionType")
    private String transactionType = "CustomerPayBillOnline";

    @JsonProperty("CallBackURL")
    private String callBackUrl;

    @JsonProperty("TransactionDesc")
    private String transactionDesc = "STK transaction initialization";

    @JsonProperty("PhoneNumber")
    public String getPhoneNumber() {
        return payorPhoneNumber;
    }
}
