package com.transport.fares.view;

import com.transport.fares.entity.DayType;
import com.transport.fares.entity.Fare;
import com.transport.fares.entity.TimePeriod;
import com.transport.liby.view.BaseView;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FareView extends BaseView<Fare> {
    public FareView(Fare entity) {super(entity);}

    public BigDecimal getFareAmount() {return entity.getFareAmount();}

    public DayType getDayType() {return entity.getDayType();}

    public TimePeriod getTimePeriod() {return entity.getTimePeriod();}

    public String getRouteId() {return entity.getRoute() != null ? entity.getRoute().getEntityId() : null;}

    public String getRouteName() {return entity.getRoute() != null ? entity.getRoute().getName() : null;}

    public String getBoardingTerminalId() {return entity.getBoardingTerminal() != null ? entity.getBoardingTerminal().getEntityId() : null;}

    public String getBoardingTerminalName() {return entity.getBoardingTerminal() != null ? entity.getBoardingTerminal().getName() : null;}

    public String getDropoffTerminalId() {return entity.getDropoffTerminal() != null ? entity.getDropoffTerminal().getEntityId() : null;}

    public String getDropoffTerminalName() {return entity.getDropoffTerminal() != null ? entity.getDropoffTerminal().getName() : null;}
}
