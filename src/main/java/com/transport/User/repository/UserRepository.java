package com.transport.User.repository;

import com.transport.User.entity.User;
import com.transport.User.entity.UserStatus;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository<U extends User> extends BaseJpaRepository<U> {
    boolean existsByPhoneNumberAndEntityIdNot(String phoneNumber, String entityId);

    boolean existsByEmailAndEntityIdNot(String email, String entityId);

    default Specification<U> userStatusIs(UserStatus userStatus) {
        return (root, query, builder) -> builder.equal(root.get("userStatus"), userStatus);
    }

    default Specification<U> nameLike(String keyWord) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + keyWord + "%");
    }

    default Specification<U> phoneNumberIs(String phoneNumber) {
        return (root, query, builder) -> builder.equal(root.get("phoneNumber"), phoneNumber);
    }

    default Specification<U> phoneNumberLike(String keyWord) {
        return (root, query, builder) -> builder.like(root.get("phoneNumber"), "%" + keyWord + "%");
    }

    default Specification<U> emailIs(String email) {
        return (root, query, builder) -> builder.equal(root.get("email"), email);
    }

    default Specification<U> emailLike(String keyWord) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + keyWord + "%");
    }
}
