package com.transport.School.Entity;

import com.transport.Routes.Entity.Route;
import com.transport.School.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class School extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @OneToMany(mappedBy = "school")
    private List<SchoolAdmin> admins = new ArrayList<>();

    @OneToMany(mappedBy = "school")
    private List<Route> routes = new ArrayList<>();
}
