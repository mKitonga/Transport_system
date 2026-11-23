package com.transport.fares.entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.liby.service.FormatUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
        name = "fare_payment",
        indexes = {
                @Index(name = "idx_fare_payment_order_id", columnList = "fare_id")
        }
)
public class FarePayment extends BaseJpaEntity {
    @ManyToOne
    private Fare fare;

    private String fareEntityId;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    private BigDecimal fareAmount;

    @Column(unique = true)
    private String transactionId;

    public void setFare(Fare fare) {
        this.fare = fare;
        if (fare != null) {
            this.fareEntityId = fare.getEntityId();
        } else {
            this.fareEntityId = null;
        }
    }
}
