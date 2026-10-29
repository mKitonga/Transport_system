package com.transport.queue.Repository;

import com.transport.queue.Entity.QueueEntry;
import com.transport.queue.Entity.QueueEntryStatus;
import com.transport.liby.repository.BaseJpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QueueEntryRepository extends BaseJpaRepository<QueueEntry> {

    @Query("""
            select q from QueueEntry q
            where q.driver.entityId = :driverId
              and q.idempotencyKey = :key
              and q.deletedAt is null
              and q.deletedByEntityId is null
            """)
    Optional<QueueEntry> findByDriverIdAndIdempotencyKey(
            @Param("driverId") String driverId,
            @Param("key") String idempotencyKey);

    boolean existsByDriver_EntityIdAndStatusInAndDeletedAtIsNullAndDeletedByEntityIdIsNull(
            String driverId,
            Set<QueueEntryStatus> statuses);

    @Query("""
            select q from QueueEntry q
            where q.driver.entityId = :driverId
              and q.status in :statuses
              and q.deletedAt is null
              and q.deletedByEntityId is null
            order by q.notifiedAt desc
            """)
    List<QueueEntry> findActiveByDriverId(
            @Param("driverId") String driverId,
            @Param("statuses") Set<QueueEntryStatus> statuses);

    @Query("""
            select q from QueueEntry q
            where q.saccoAdmin.entityId = :adminId
              and q.terminal.entityId = :terminalId
              and q.status in :statuses
              and q.deletedAt is null
              and q.deletedByEntityId is null
            order by q.queuePosition asc
            """)
    List<QueueEntry> findActiveForAdminAndTerminal(
            @Param("adminId") String adminId,
            @Param("terminalId") String terminalId,
            @Param("statuses") Set<QueueEntryStatus> statuses);

    @Query("select coalesce(max(q.queuePosition), 0) from QueueEntry q where q.terminal.entityId = :terminalId")
    int findHighestQueuePosition(@Param("terminalId") String terminalId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select q from QueueEntry q
            where q.entityId = :entryId
              and q.deletedAt is null
              and q.deletedByEntityId is null
            """)
    Optional<QueueEntry> findActiveByEntityIdForUpdate(@Param("entryId") String entryId);
}
