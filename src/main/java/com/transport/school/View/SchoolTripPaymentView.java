package com.transport.school.View;

import com.transport.liby.view.BaseView;
import com.transport.school.Entity.SchoolGrade;
import com.transport.school.Entity.SchoolTripPayment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolTripPaymentView extends BaseView<SchoolTripPayment> {

    public SchoolTripPaymentView(SchoolTripPayment entity) {
        super(entity);
    }

    public String getParentId() {
        return entity.getParent() != null ? entity.getParent().getEntityId() : null;
    }

    public String getParentName() {
        return entity.getParent() != null ? entity.getParent().getName() : null;
    }

    public String getStudentName() {
        return entity.getStudentName();
    }

    public SchoolGrade getGrade() {
        return entity.getGrade();
    }

    public String getTripType() {
        return entity.getTripType();
    }

    public String getTripId() {
        return entity.getTripId();
    }

    public BigDecimal getAmount() {
        return entity.getAmount();
    }

    public String getPhoneNumber() {
        return entity.getPhoneNumber();
    }

    public String getMpesaReference() {
        return entity.getMpesaReference();
    }

    public String getStatus() {
        return entity.getStatus();
    }

    public LocalDateTime getApprovedAt() {
        return entity.getApprovedAt();
    }
}
