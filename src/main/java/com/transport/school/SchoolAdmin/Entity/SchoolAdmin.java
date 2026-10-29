package com.transport.school.SchoolAdmin.Entity;

import com.transport.school.Entity.School;
import com.transport.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SchoolAdmin extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "school_location")
    private String location;
}
