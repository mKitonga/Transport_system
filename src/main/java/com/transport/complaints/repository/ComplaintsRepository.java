package com.transport.complaints.repository;

import com.transport.complaints.entity.ComplaintService;
import com.transport.complaints.entity.ComplaintStatus;
import com.transport.complaints.entity.Complaints;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface ComplaintsRepository extends BaseJpaRepository<Complaints> {
    default Specification<Complaints> serviceIs(ComplaintService service) {
        return ((root, query, criteriaBuilder) ->  criteriaBuilder.equal(root.get("service"), service));
    }

    default Specification<Complaints> statusIs(ComplaintStatus status) {
        return ((root, query, criteriaBuilder) ->  criteriaBuilder.equal(root.get("status"), status));
    }

    default Specification<Complaints> saccoEntityIdIs(String saccoEntityId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("sacco").get("entityId"), saccoEntityId);
    }

    default Specification<Complaints> schoolEntityIdIs(String schoolEntityId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("school").get("entityId"), schoolEntityId);
    }

    default Specification<Complaints> routeEntityIdIs(String routeEntityId) {
        return (root,query, criteriaBuilder) -> criteriaBuilder.equal(root.join("route").get("entityId"), routeEntityId);
    }

    default Specification<Complaints> searchLike(String query) {
        String searchQuery = "%" + query.trim().toLowerCase() + "%";
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("complaintMessage")), searchQuery),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("passengerPhoneNumber")), searchQuery),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("passengerEmail")), searchQuery)
                );
    }
}
