package com.transport.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing the profile details of a School Admin.
 * Linked one-to-one with a {@link User}.
 */
@Entity
@Table(name = "school_admin_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolAdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "school_location", nullable = false)
    private String schoolLocation;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
