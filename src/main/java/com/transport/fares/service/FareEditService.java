package com.transport.fares.service;

import com.transport.fares.entity.Fare;
import com.transport.fares.form.EditFaresForm;
import com.transport.fares.form.RegisterFaresForm;
import com.transport.fares.repository.FareRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import com.transport.routes.Service.RouteReadService;
import com.transport.routes.Service.TerminalReadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FareEditService extends BaseJpaRepoEditService<Fare, FareRepository> {
    private RouteReadService routeReadService;
    private TerminalReadService terminalReadService;
    private FareReadService fareReadService;

    @Transactional
    public Fare registerFare(RegisterFaresForm form){
        validateTerminals(form.getBoardingTerminalId(), form.getDropoffTerminalId());
       checkFareExists(null, form);

        var route = routeReadService.findByEntityId(form.getRouteId());
        var boardingTerminal = terminalReadService.findByEntityId(form.getBoardingTerminalId());
        var dropoffTerminal = terminalReadService.findByEntityId(form.getDropoffTerminalId());

        var fare = new Fare();
        fare.setRoute(route);
        fare.setBoardingTerminal(boardingTerminal);
        fare.setDropoffTerminal(dropoffTerminal);
        fare.setDayType(form.getDayType());
        fare.setTimePeriod(form.getTimePeriod());
        fare.setFareAmount(form.getFareAmount());
        fare.setCreatedByEntityId(form.getSessionUserId());

        fare = save(fare, form.getSessionUserId());

        return fare;
    }

    @Transactional
    public Fare editFare(String fareId, EditFaresForm form) {
        validateTerminals(form.getBoardingTerminalId(), form.getDropoffTerminalId());

        Fare fare = findByEntityId(fareId);

        checkFareExists(fareId, form);

        var route = routeReadService.findByEntityId(form.getRouteId());
        var boardingTerminal = terminalReadService.findByEntityId(form.getBoardingTerminalId());
        var dropoffTerminal = terminalReadService.findByEntityId(form.getDropoffTerminalId());

        fare.setRoute(route);
        fare.setBoardingTerminal(boardingTerminal);
        fare.setDropoffTerminal(dropoffTerminal);
        fare.setDayType(form.getDayType());
        fare.setTimePeriod(form.getTimePeriod());
        fare.setFareAmount(form.getFareAmount());

        fare = save(fare, form.getSessionUserId());

        return fare;
    }

    private void checkFareExists(String fareId, EditFaresForm form) {
        var spec = repository.notDeleted()
                .and(repository.routeEntityIdIs(form.getRouteId()))
                .and(repository.boardingTerminalEntityIdIs(form.getBoardingTerminalId()))
                .and(repository.dropoffTerminalEntityIdIs(form.getDropoffTerminalId()))
                .and(repository.dayTypeIs(form.getDayType()))
                .and(repository.timePeriodIs(form.getTimePeriod()));

        if (fareId != null) {
            spec = spec.and(repository.entityIdNot(fareId));
        }

        boolean exists = repository.exists(spec);

        if (exists) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.fare");
        }
    }

    private void validateTerminals(String boardingTerminalId, String dropoffTerminalId) {
        if (boardingTerminalId != null && boardingTerminalId.equalsIgnoreCase(dropoffTerminalId)) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.fare.terminals.must.differ");
        }
    }

    @Autowired
    public void setRouteReadService(RouteReadService routeReadService) {this.routeReadService = routeReadService;}

    @Autowired
    public void setTerminalReadService(TerminalReadService terminalReadService) {this.terminalReadService = terminalReadService;}

    @Autowired
    public void setFareReadService(FareReadService fareReadService) {this.fareReadService = fareReadService;}
}
