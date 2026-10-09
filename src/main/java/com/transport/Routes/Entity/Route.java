package com.transport.Routes.Entity;

import com.transport.Sacco.Entity.Sacco;
import com.transport.School.Entity.School;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Route extends BaseJpaEntity {
    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "route_terminals",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "terminal_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_route_terminal",
                    columnNames = {"route_id", "terminal_id"}
            )
    )
    private Set<Terminal> terminals = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sacco_id")
    private Sacco sacco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @PrePersist
    @PreUpdate
    private void validateOwner() {
        if ((sacco == null) == (school == null)) {
            throw new IllegalStateException("A route must belong to exactly one Sacco or School.");
        }
    }
}
