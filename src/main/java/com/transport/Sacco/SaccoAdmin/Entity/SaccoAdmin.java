package com.transport.Sacco.SaccoAdmin.Entity;

import com.transport.Routes.Entity.Terminal;
import com.transport.Sacco.Entity.Sacco;
import com.transport.User.entity.User;
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
