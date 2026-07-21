package com.transport.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing the profile details of a Parent.
 * Linked one-to-one with a {@link User}.
 */
@Entity
@Table(name = "parent_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "child_name", nullable = false)
    private String childName;

    @Column(name = "child_school", nullable = false)
    private String childSchool;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
