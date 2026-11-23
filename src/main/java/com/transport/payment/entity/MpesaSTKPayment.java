package com.transport.payment.entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.liby.service.FormatUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(
        name = "mpesa_stk_payment"
)
public class MpesaSTKPayment extends BaseJpaEntity {
    private String phoneNumber;
    private String transactionId;
    private String parentBusinessShortCode;
    private String childBusinessShortCode;
    private String accountReference;
    private String checkoutRequestId;
    private String initiatorIdentifierId;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    private BigDecimal fareAmount;

    public BigDecimal getAmount() {
        return fareAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.fareAmount = amount;
    }

    @Column(length = 1048)
    private String initialResponseDump;

    @Column(length = 1048)
    private String callBackResponseDump;

    @Enumerated(EnumType.STRING)
    private MpesaSTKPushPaymentStatus mpesaSTKPushPaymentStatus;
}
