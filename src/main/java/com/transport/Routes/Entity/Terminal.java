package com.transport.Routes.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Terminal extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "terminals")
    private Set<Route> routes = new HashSet<>();
}
