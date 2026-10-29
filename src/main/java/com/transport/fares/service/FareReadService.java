package com.transport.fares.service;

import com.transport.fares.entity.DayType;
import com.transport.fares.entity.Fare;
import com.transport.fares.entity.TimePeriod;
import com.transport.fares.form.FetchFaresForm;
import com.transport.fares.repository.FareRepository;
import com.transport.fares.view.FareView;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FareReadService extends BaseJpaRepoReadService<Fare, FareRepository> {

    @Transactional(readOnly = true)
    public Page<FareView> listFares(FetchFaresForm form) {
        Specification<Fare> spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getRouteId())) {
            spec = spec.and(repository.routeEntityIdIs(form.getRouteId().trim()));
        }

        return repository.findAll(spec, repository.defaultPageable(form))
                .map(FareView::new);
    }

    @Transactional(readOnly = true)
    public Optional<Fare> findFare(String routeId, String boardingTerminalId, String dropoffTerminalId, DayType dayType, TimePeriod timePeriod) {
        Specification<Fare> spec = repository.notDeleted()
                .and(repository.routeEntityIdIs(routeId))
                .and(repository.boardingTerminalEntityIdIs(boardingTerminalId))
                .and(repository.dropoffTerminalEntityIdIs(dropoffTerminalId))
                .and(repository.dayTypeIs(dayType))
                .and(repository.timePeriodIs(timePeriod));

        return repository.findOne(spec);
    }

    @Transactional(readOnly = true)
    public boolean existsFare(
            String routeId,
            String boardingTerminalId,
            String dropoffTerminalId,
            DayType dayType,
            TimePeriod timePeriod
    ) {
        return findFare(routeId, boardingTerminalId, dropoffTerminalId, dayType, timePeriod).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean existsFareExcluding(
            String routeId,
            String boardingTerminalId,
            String dropoffTerminalId,
            DayType dayType,
            TimePeriod timePeriod,
            String excludeEntityId
    ) {
        Specification<Fare> spec = repository.notDeleted()
                .and(repository.entityIdNot(excludeEntityId))
                .and(repository.routeEntityIdIs(routeId))
                .and(repository.boardingTerminalEntityIdIs(boardingTerminalId))
                .and(repository.dropoffTerminalEntityIdIs(dropoffTerminalId))
                .and(repository.dayTypeIs(dayType))
                .and(repository.timePeriodIs(timePeriod));

        return repository.exists(spec);
    }

    public CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }
}