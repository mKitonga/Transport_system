package com.transport.sts.entity;

import com.transport.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sts_users")
public class StsUser extends User {
}
