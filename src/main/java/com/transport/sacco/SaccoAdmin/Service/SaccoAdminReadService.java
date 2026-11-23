package com.transport.sacco.SaccoAdmin.Service;

import com.transport.complaints.entity.ComplaintStatus;
import com.transport.complaints.repository.ComplaintsRepository;
import com.transport.queue.Entity.QueueEntryStatus;
import com.transport.queue.Repository.QueueEntryRepository;
import com.transport.routes.Repository.RouteRepository;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.sacco.SaccoAdmin.View.SaccoAdminOverviewView;
import com.transport.sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.user.entity.UserStatus;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SaccoAdminReadService extends BaseJpaRepoReadService<SaccoAdmin, SaccoAdminRepository> {

    private SaccoDriverRepository saccoDriverRepository;
    private RouteRepository routeRepository;
    private QueueEntryRepository queueEntryRepository;
    private ComplaintsRepository complaintsRepository;

    public Optional<SaccoAdmin> findActiveAdminByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(entityId);
    }

    public SaccoAdmin findActiveAdminByStationId(String stationId) {
        var admins = repository.findAllByStation_EntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(stationId);
        if (admins.size() != 1) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }
        return admins.get(0);
    }

    @Transactional(readOnly = true)
    public SaccoAdminOverviewView getOverview(String adminEntityId) {
        SaccoAdmin admin = findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        String saccoId = admin.getSacco().getEntityId();

        long pendingDrivers = saccoDriverRepository.count(
                saccoDriverRepository.notDeleted()
                        .and(saccoDriverRepository.saccoEntityIdIs(saccoId))
                        .and(saccoDriverRepository.userStatusIs(UserStatus.PENDING_APPROVAL))
        );

        long activeRoutes = routeRepository.count(
                routeRepository.notDeleted()
                        .and(routeRepository.saccoEntityIdIs(saccoId))
        );

        long queueWaiting = queueEntryRepository.count(
                queueEntryRepository.notDeleted()
                        .and(queueEntryRepository.saccoAdminEntityIdIs(admin.getEntityId()))
                        .and(queueEntryRepository.statusIs(QueueEntryStatus.WAITING))
        );

        long openComplaints = complaintsRepository.count(
                complaintsRepository.notDeleted()
                        .and(complaintsRepository.saccoEntityIdIs(saccoId))
                        .and(complaintsRepository.statusIs(ComplaintStatus.OPEN))
        );

        return SaccoAdminOverviewView.builder()
                .pendingDriverApprovals(pendingDrivers)
                .activeRoutes(activeRoutes)
                .queueWaiting(queueWaiting)
                .openComplaints(openComplaints)
                .build();
    }

    @Autowired
    public void setSaccoDriverRepository(SaccoDriverRepository saccoDriverRepository) {
        this.saccoDriverRepository = saccoDriverRepository;
    }

    @Autowired
    public void setRouteRepository(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Autowired
    public void setQueueEntryRepository(QueueEntryRepository queueEntryRepository) {
        this.queueEntryRepository = queueEntryRepository;
    }

    @Autowired
    public void setComplaintsRepository(ComplaintsRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
    }
}
