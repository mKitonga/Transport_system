package com.transport.sacco.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.sacco.Entity.SaccoMessage;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface SaccoMessageRepository extends BaseJpaRepository<SaccoMessage> {

    default Specification<SaccoMessage> schoolAdminEntityIdIs(String adminEntityId) {
        return (root, q, cb) -> cb.equal(root.join("schoolAdmin").get("entityId"), adminEntityId);
    }

    default Specification<SaccoMessage> saccoAdminEntityIdIs(String adminEntityId) {
        return (root, q, cb) -> cb.equal(root.join("saccoAdmin").get("entityId"), adminEntityId);
    }
}
