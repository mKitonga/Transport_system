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
        return (root,cb,cq) -> cq.equal(root.get("grade"), schoolGrade);
    }

    default Specification<Trip> tripStatusIs(TripStatus tripStatus){
        return (root,cb,cq) -> cq.equal(root.get("status"), tripStatus);
    }

    default Specification<Trip> tripTypeIs(TripType tripType){
        return (root, cb, cq) -> cq.equal(root.get("tripType"), tripType);
    }

    default Specification<Trip> assignedDriverEntityIdIs(String driverEntityId) {
        return (root, cb, cq) -> cq.equal(root.get("assignedDriver").get("entityId"), driverEntityId);
    }

    default Specification<Trip> schoolAdminEntityIdIs(String schoolAdminEntityId) {
        return (root, cb, cq) -> cq.equal(root.join("schoolAdmin").get("entityId"), schoolAdminEntityId);
    }

    default Specification<Trip> schoolEntityIdIs(String schoolEntityId) {
        return (root, cb, cq) -> cq.equal(root.join("schoolAdmin").join("school").get("entityId"), schoolEntityId);
    }
}
