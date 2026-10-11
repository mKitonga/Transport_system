package com.transport.Sacco.SaccoDriver.Entity;

import com.transport.Routes.Entity.Route;
import com.transport.Sacco.Entity.Sacco;
import com.transport.User.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SaccoDriver")
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
