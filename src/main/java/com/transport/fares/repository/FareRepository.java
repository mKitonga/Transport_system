package com.transport.fares.repository;

import com.transport.fares.entity.DayType;
import com.transport.fares.entity.Fare;
import com.transport.fares.entity.TimePeriod;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface FareRepository extends BaseJpaRepository<Fare> {

    default Specification<Fare> routeEntityIdIs(String routeEntityId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("route").get("entityId"), routeEntityId);
    }

    default Specification<Fare> boardingTerminalEntityIdIs(String terminalEntityId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("boardingTerminal").get("entityId"), terminalEntityId);
    }

    default Specification<Fare> dropoffTerminalEntityIdIs(String terminalEntityId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("dropoffTerminal").get("entityId"), terminalEntityId);
    }

    default Specification<Fare> dayTypeIs(DayType dayType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dayType"), dayType);
    }

    default Specification<Fare> timePeriodIs(TimePeriod timePeriod) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("timePeriod"), timePeriod);
    }
}
