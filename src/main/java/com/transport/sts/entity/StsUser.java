package com.transport.sts.entity;

import com.transport.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * STS (System Token Service) user entity.
 * Extends the common {@link User} base class so it inherits id, name, email,
 * phone, status, timestamps, etc.
 */
@Entity
@Table(name = "sts_users")
public class StsUser extends User {
    // Add STS‑specific fields here if needed in the future.
}
