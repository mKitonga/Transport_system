package com.transport.queue.Service;

import com.transport.queue.Entity.QueueEntry;
import com.transport.queue.Entity.QueueEntryStatus;
import com.transport.queue.Form.CreateArrivalForm;
import com.transport.queue.Form.DispatchQueueEntryForm;
import com.transport.queue.Repository.QueueEntryRepository;
import com.transport.routes.Entity.Route;
import com.transport.routes.Service.RouteEditService;
import com.transport.routes.Service.TerminalReadService;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.sacco.SaccoDriver.Service.SaccoDriverEditService;
import com.transport.sacco.SaccoDriver.Service.SaccoDriverReadService;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.SessionUserIdForm;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class QueueEntryEditService extends BaseJpaRepoEditService<QueueEntry, QueueEntryRepository> {

    private QueueEntryReadService queueEntryReadService;
    private SaccoDriverReadService saccoDriverReadService;
    private SaccoDriverEditService saccoDriverEditService;
    private SaccoAdminReadService saccoAdminReadService;
    private TerminalReadService terminalReadService;
    private RouteEditService routeEditService;

    @Transactional
    public QueueEntry createArrival(CreateArrivalForm form, String idempotencyKey) {
        String driverId = form.getSessionUserId();
        String terminalId = form.getTerminalId();
        if (idempotencyKey == null || idempotencyKey.isBlank() || idempotencyKey.length() > 128) {
            throw badRequest("error.invalid.idempotency.key");
        }
        var driver = saccoDriverReadService.findActiveByEntityIdForUpdate(driverId);

        var priorRequest = queueEntryReadService.findArrivalByIdempotencyKey(driverId, idempotencyKey);
        if (priorRequest.isPresent()) {
            QueueEntry priorEntry = priorRequest.get();
            if (!priorEntry.getTerminal().getEntityId().equals(terminalId)) {
                throw badRequest("error.queue.idempotency.key.reused");
            }
            return priorEntry;
        }

        if (queueEntryReadService.hasActiveEntry(driverId)) {
            throw new CommonRuntimeException(
                    ExceptionType.ALREADY_EXISTS,
                    "error.queue.active.entry.exists"
            );
        }

        var terminal = terminalReadService.findActiveByEntityIdForUpdate(terminalId);
        var admin = saccoAdminReadService.findActiveAdminByStationId(terminalId);
        if (driver.getSacco() == null || admin.getSacco() == null || !driver.getSacco().getEntityId().equals(admin.getSacco().getEntityId())) {
            throw notFound();
        }

        var entry = new QueueEntry();
        entry.setDriver(driver);
        entry.setSaccoAdmin(admin);
        entry.setTerminal(terminal);
        entry.setIdempotencyKey(idempotencyKey);
        entry.setNotifiedAt(LocalDateTime.now());
        entry.setQueuePosition(queueEntryReadService.nextPositionForTerminal(terminalId));
        entry.setStatus(QueueEntryStatus.WAITING);
        entry.setCreatedByEntityId(driverId);

        entry = save(entry, form.getSessionUserId());

        return entry;
    }

    @Transactional
    public QueueEntry dispatch(String entryId, DispatchQueueEntryForm form) {
        String adminId = form.getSessionUserId();
        String routeId = form.getRouteId();
        var entry = findEntryForUpdate(entryId);
        assertEntryBelongsToAdmin(entry, adminId);

        if (entry.getStatus() == QueueEntryStatus.DISPATCHED) {
            if (entry.getAssignedRoute() != null
                    && entry.getAssignedRoute().getEntityId().equals(routeId)) {
                return entry;
            }
            throw badRequest("error.queue.entry.invalid.status");
        }
        if (entry.getStatus() != QueueEntryStatus.WAITING) {
            throw badRequest("error.queue.entry.invalid.status");
        }

        var route = routeEditService.findOwnedRoute(routeId, adminId);
        var admin = saccoAdminReadService.findActiveAdminByEntityId(adminId)
                .orElseThrow(this::notFound);
        if (route.getSacco() == null
                || entry.getDriver().getSacco() == null
                || admin.getSacco() == null
                || !route.getSacco().getEntityId().equals(entry.getDriver().getSacco().getEntityId())
                || !route.getSacco().getEntityId().equals(admin.getSacco().getEntityId())
                || !routeContainsTerminal(route, entry.getTerminal().getEntityId())) {
            throw notFound();
        }

        entry.setAssignedRoute(route);
        entry.setAssignedRouteName(route.getName());
        entry.setStatus(QueueEntryStatus.DISPATCHED);
        saccoDriverEditService.assignRoute(entry.getDriver().getEntityId(), route, form.getSessionUserId());

        entry = save(entry, form.getSessionUserId());
        return entry;
    }

    @Transactional
    public QueueEntry markLeft(String entryId, SessionUserIdForm form) {
        String adminId = form.getSessionUserId();
        var entry = findEntryForUpdate(entryId);
        assertEntryBelongsToAdmin(entry, adminId);

        if (entry.getStatus() == QueueEntryStatus.LEFT) {
            return entry;
        }
        if (entry.getStatus() != QueueEntryStatus.WAITING
                && entry.getStatus() != QueueEntryStatus.DISPATCHED) {
            throw badRequest("error.queue.entry.invalid.status");
        }

        entry.setStatus(QueueEntryStatus.LEFT);
        entry.setDepartedAt(LocalDateTime.now());
        if (entry.getAssignedRoute() != null) {
            saccoDriverEditService.clearAssignedRoute(
                    entry.getDriver().getEntityId(),
                    entry.getAssignedRoute().getEntityId(),
                    form.getSessionUserId()
            );
        }
        entry = save(entry, form.getSessionUserId());
        return entry;
    }

    private QueueEntry findEntryForUpdate(String entryId) {
        return repository.findActiveByEntityIdForUpdate(entryId)
                .orElseThrow(this::notFound);
    }

    private void assertEntryBelongsToAdmin(QueueEntry entry, String adminId) {
        SaccoAdmin admin = saccoAdminReadService.findActiveAdminByEntityId(adminId)
                .orElseThrow(this::notFound);
        if (entry.getSaccoAdmin() == null
                || !entry.getSaccoAdmin().getEntityId().equals(adminId)
                || admin.getStation() == null
                || !admin.getStation().getEntityId().equals(entry.getTerminal().getEntityId())) {
            throw notFound();
        }
    }

    private boolean routeContainsTerminal(Route route, String terminalId) {
        return route.getTerminals().stream()
                .anyMatch(terminal -> terminal.getEntityId().equals(terminalId));
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }

    private CommonRuntimeException badRequest(String messageKey) {
        return new CommonRuntimeException(ExceptionType.BAD_REQUEST, messageKey);
    }

    @Autowired
    public void setQueueEntryReadService(QueueEntryReadService queueEntryReadService) {
        this.queueEntryReadService = queueEntryReadService;
    }

    @Autowired
    public void setSaccoDriverReadService(SaccoDriverReadService saccoDriverReadService) {
        this.saccoDriverReadService = saccoDriverReadService;
    }

    @Autowired
    public void setSaccoDriverEditService(SaccoDriverEditService saccoDriverEditService) {
        this.saccoDriverEditService = saccoDriverEditService;
    }

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService saccoAdminReadService) {
        this.saccoAdminReadService = saccoAdminReadService;
    }

    @Autowired
    public void setTerminalReadService(TerminalReadService terminalReadService) {
        this.terminalReadService = terminalReadService;
    }

    @Autowired
    public void setRouteEditService(RouteEditService routeEditService) {
        this.routeEditService = routeEditService;
    }
}
