package com.transport.sacco.SaccoDriver.Entity;

import com.transport.routes.Entity.Route;
import com.transport.sacco.Entity.Sacco;
import com.transport.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SaccoDriver extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sacco_id")
    private Sacco sacco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_route_id")
    private Route assignedRoute;

    @Column
    private String numberPlate;

    @Column
    private String matatuName;
}
