package com.transport.Permissions.entity;

import com.transport.User.entity.UserType;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission extends BaseJpaEntity {

    @Column(unique = true, length = 50)
    private String permissionName;

    @Column(unique = true, length = 256)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_type_permission",
            joinColumns = @JoinColumn(name = "permission_id", nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = {"permission_id", "user_type"})
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 32)
    private Set<UserType> userTypes = new HashSet<>();
}
