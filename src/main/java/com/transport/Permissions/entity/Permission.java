package com.transport.Permissions.entity;

import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Permission extends BaseJpaEntity {

    @Column(unique = true, length = 50)
    private String permissionName;

    @Column(unique = true, length = 256)
    private String description;
}
