package com.transport.user.service;

import com.transport.sts.entity.StsUser;
import com.transport.sts.form.StsUserFetchForm;
import com.transport.sts.repository.StsUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StsUserReadService extends BaseUserReadService<StsUser, StsUserRepository> {

    public Page<StsUser> listUsers(StsUserFetchForm form) {
        Specification<StsUser> spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(
                    repository.nameLike(form.getQuery())
                            .or(repository.emailLike(form.getQuery()))
                            .or(repository.phoneNumberLike(form.getQuery()))
            );
        }

        if (form.getUserStatus() != null) {
            spec = spec.and(repository.userStatusIs(form.getUserStatus()));
        }

        LocalDateTime startDate = form.getStartDate();
        LocalDateTime endDate = form.getEndDate();
        if (startDate != null || endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                var createdAt = root.<LocalDateTime>get("createdAt");
                if (startDate != null && endDate != null) {
                    return criteriaBuilder.between(createdAt, startDate, endDate);
                }
                if (startDate != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(createdAt, startDate);
                }
                return criteriaBuilder.lessThanOrEqualTo(createdAt, endDate);
            });
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }
}
