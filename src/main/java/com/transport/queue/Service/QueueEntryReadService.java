package com.transport.queue.Service;

import com.transport.queue.Entity.QueueEntry;
import com.transport.queue.Entity.QueueEntryStatus;
import com.transport.queue.Repository.QueueEntryRepository;
import com.transport.sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QueueEntryReadService extends BaseJpaRepoReadService<QueueEntry, QueueEntryRepository> {

    private static final Set<QueueEntryStatus> ACTIVE_STATUSES =
            Set.of(QueueEntryStatus.WAITING, QueueEntryStatus.DISPATCHED);

    private final SaccoAdminReadService saccoAdminReadService;

    public QueueEntryReadService(SaccoAdminReadService saccoAdminReadService) {
        this.saccoAdminReadService = saccoAdminReadService;
    }

    @Transactional(readOnly = true)
    public Optional<QueueEntry> findArrivalByIdempotencyKey(String driverId, String idempotencyKey) {
        return repository.findByDriverIdAndIdempotencyKey(driverId, idempotencyKey);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveEntry(String driverId) {
        return repository.existsByDriver_EntityIdAndStatusInAndDeletedAtIsNullAndDeletedByEntityIdIsNull(
                driverId,
                ACTIVE_STATUSES
        );
    }

    @Transactional(readOnly = true)
    public Optional<QueueEntry> findCurrentEntry(String driverId) {
        return repository.findActiveByDriverId(driverId, ACTIVE_STATUSES)
                .stream()
                .findFirst();
    }

    @Transactional(readOnly = true)
    public List<QueueEntry> listActiveEntriesForAdmin(String adminId) {
        var admin = saccoAdminReadService.findActiveAdminByEntityId(adminId)
                .orElseThrow(this::notFound);
        if (admin.getStation() == null) {
            throw notFound();
        }
        return repository.findActiveForAdminAndTerminal(
                adminId,
                admin.getStation().getEntityId(),
                ACTIVE_STATUSES
        );
    }

    @Transactional(readOnly = true)
    public int nextPositionForTerminal(String terminalId) {
        return repository.findHighestQueuePosition(terminalId) + 1;
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }
}
