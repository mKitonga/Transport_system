package com.transport.trips.repository;

import com.transport.school.Entity.SchoolGrade;
import com.transport.trips.entity.Trip;
import com.transport.trips.entity.TripStatus;
import com.transport.trips.entity.TripType;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends BaseJpaRepository<Trip> {
    default Specification<Trip> nameLike(String query){
        return (root,cb,cq) -> cq.like(root.get("name"), "%" + query + "%");
    }

    default Specification<Trip> schoolGradeIs(SchoolGrade schoolGrade){
        return (root,cb,cq) -> cq.equal(root.get("schoolGrade"), schoolGrade);
    }

    default Specification<Trip> tripStatusIs(TripStatus tripStatus){
        return (root,cb,cq) -> cq.equal(root.get("tripStatus"), tripStatus);
    }

    default Specification<Trip> tripTypeIs(TripType tripType){
        return (root, cb, cq) -> cq.equal(root.get("tripType"), tripType);
    }
}
