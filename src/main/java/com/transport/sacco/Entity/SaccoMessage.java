package com.transport.sacco.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sacco_messages")
@Getter
@Setter
public class SaccoMessage extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_admin_id", nullable = false)
    private SchoolAdmin schoolAdmin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sacco_admin_id", nullable = false)
    private SaccoAdmin saccoAdmin;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "location")
    private String location;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "SENT";

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();
}
