package com.transport.sacco.SaccoDriver.View;

import com.transport.liby.view.BaseView;
import com.transport.sacco.SaccoDriver.Entity.PassengerPayment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PassengerPaymentView extends BaseView<PassengerPayment> {

    public PassengerPaymentView(PassengerPayment entity) {
        super(entity);
    }

    public String getDriverId() {
        return entity.getDriver() != null ? entity.getDriver().getEntityId() : null;
    }

    public String getDriverName() {
        return entity.getDriver() != null ? entity.getDriver().getName() : null;
    }

    public String getRouteId() {
        return entity.getRoute() != null ? entity.getRoute().getEntityId() : null;
    }

    public String getRouteName() {
        return entity.getRoute() != null ? entity.getRoute().getName() : null;
    }

    public String getBoardingTerminalId() {
        return entity.getBoardingTerminal() != null ? entity.getBoardingTerminal().getEntityId() : null;
    }

    public String getBoardingTerminalName() {
        return entity.getBoardingTerminal() != null ? entity.getBoardingTerminal().getName() : null;
    }

    public String getDropoffTerminalId() {
        return entity.getDropoffTerminal() != null ? entity.getDropoffTerminal().getEntityId() : null;
    }

    public String getDropoffTerminalName() {
        return entity.getDropoffTerminal() != null ? entity.getDropoffTerminal().getName() : null;
    }

    public String getPassengerPhone() {
        return entity.getPassengerPhone();
    }

    public BigDecimal getFareAmount() {
        return entity.getFareAmount();
    }

    public String getPromptStatus() {
        return entity.getPromptStatus();
    }

    public LocalDateTime getClearedAt() {
        return entity.getClearedAt();
    }

    public LocalDateTime getPromptRequestedAt() {
        return entity.getPromptRequestedAt();
    }
}
