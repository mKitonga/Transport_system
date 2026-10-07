package com.transport.liby.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseJpaEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, updatable = false)
    private String entityId;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(updatable = false)
    private String createdByEntityId;

    private LocalDateTime updatedAt = LocalDateTime.now();
    private String updatedByEntityId;

    private LocalDateTime deletedAt;
    private String deletedByEntityId;

    public BaseJpaEntity() {
        generateEntityId();
    }

    private void generateEntityId() {
        if (getEntityId() == null) {
            String uuid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 24);
            String entityId = String.format(
                    "%s-%s",
                    getClass().getSimpleName().toLowerCase(),
                    uuid
            );

            setEntityId(entityId);
        }
    }

    public boolean getIsDeleted() {
        return getDeletedAt() != null || getDeletedByEntityId() != null;
    }
}
