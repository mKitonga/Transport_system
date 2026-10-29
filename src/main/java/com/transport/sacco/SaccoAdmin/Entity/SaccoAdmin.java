package com.transport.sacco.SaccoAdmin.Entity;

import com.transport.routes.Entity.Terminal;
import com.transport.sacco.Entity.Sacco;
import com.transport.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SaccoAdmin extends User {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sacco_id")
    private Sacco sacco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", unique = true)
    private Terminal station;
}
