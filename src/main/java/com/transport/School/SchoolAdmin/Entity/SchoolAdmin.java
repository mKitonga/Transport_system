package com.transport.School.SchoolAdmin.Entity;

import com.transport.School.Entity.School;
import com.transport.User.entity.User;
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
