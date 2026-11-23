package com.transport.payment.entity;

import com.transport.fares.validator.UniqueTransactionId;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Payment {
    private String accountReference;

    @UniqueTransactionId
    private String transactionId;

    private BigDecimal fareAmount;

    public BigDecimal getAmount() {
        return fareAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.fareAmount = amount;
    }

    public BigDecimal getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
    }
}
