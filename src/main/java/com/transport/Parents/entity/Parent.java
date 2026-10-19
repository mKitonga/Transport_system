package com.transport.Parents.entity;

import com.transport.School.Entity.School;
import com.transport.School.Entity.SchoolGrade;
import com.transport.User.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(indexes = {
        @Index(name = "idx_parent_school_name", columnList = "school_name"),
        @Index(name = "idx_parent_school_location", columnList = "school_location")
})
@Getter
@Setter
public class Parent extends User {
    @Column
    private String studentName;

    @Column
    private String admissionNumber;

    @Column
    private SchoolGrade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "school_location")
    private String schoolLocation;
}
