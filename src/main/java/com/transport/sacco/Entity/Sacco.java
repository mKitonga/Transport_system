package com.transport.sacco.Entity;

import com.transport.routes.Entity.Route;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Sacco extends BaseJpaEntity {

    @Column
    private String name;

    @OneToMany(mappedBy = "sacco")
    private List<SaccoAdmin> admins = new ArrayList<>();

    @OneToMany(mappedBy = "sacco")
    private List<Route> routes = new ArrayList<>();
}
