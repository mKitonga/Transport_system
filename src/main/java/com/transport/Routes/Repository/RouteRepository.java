package com.transport.Routes.Repository;

import com.transport.Routes.Entity.Route;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface RouteRepository extends BaseJpaRepository<Route> {

    default Specification<Route> nameLike(String query) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + query.toLowerCase() + "%"
                );
    }

    default Specification<Route> saccoEntityIdIs(String saccoEntityId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("sacco").get("entityId"), saccoEntityId);
    }

    default Specification<Route> schoolEntityIdIs(String schoolEntityId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("school").get("entityId"), schoolEntityId);
    }

    default Specification<Route> terminalEntityIdIs(String terminalEntityId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("terminals").get("entityId"), terminalEntityId);
    }
}
