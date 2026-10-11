package com.transport.Sacco.SaccoDriver.Service;

import com.transport.Routes.Entity.Route;
import com.transport.Sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.Sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaccoDriverEditService extends BaseJpaRepoEditService<SaccoDriver, SaccoDriverRepository> {

    @Transactional
    public SaccoDriver assignRoute(String driverId, Route route, String updatedById) {
        SaccoDriver driver = findByEntityId(driverId);
        if (driver.getSacco() == null
                || route.getSacco() == null
                || !driver.getSacco().getEntityId().equals(route.getSacco().getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        driver.setAssignedRoute(route);
        return save(driver, updatedById);
    }

    @Transactional
    public void clearAssignedRoute(String driverId, String routeId, String updatedById) {
        SaccoDriver driver = findByEntityId(driverId);
        if (driver.getAssignedRoute() != null
                && driver.getAssignedRoute().getEntityId().equals(routeId)) {
            driver.setAssignedRoute(null);
            save(driver, updatedById);
        }
    }
}
