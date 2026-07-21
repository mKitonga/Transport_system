package com.transport.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sacco_driver_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaccoDriverProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sacco_name", nullable = false)
    private String saccoName;

    @Column(name = "number_plate", nullable = false, unique = true)
    private String numberPlate;

    @Column(name = "matatu_name", nullable = false, unique = true)
    private String matatuName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}

