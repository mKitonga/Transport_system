package com.transport.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing the profile details of a SACCO Admin.
 * Linked one-to-one with a {@link User}.
 */
@Entity
@Table(name = "sacco_admin_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaccoAdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sacco_name", nullable = false)
    private String saccoName;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
