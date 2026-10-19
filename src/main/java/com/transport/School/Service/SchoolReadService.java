package com.transport.School.Service;

import com.transport.School.Entity.School;
import com.transport.School.Repository.SchoolRepository;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SchoolReadService extends BaseJpaRepoReadService<School, SchoolRepository> {
    public Page<School> listSchools(BaseFetchForm form) {
        var spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(
                    repository.nameLike(form.getQuery())
            );
        }
        return repository.findAll(
                spec,
                repository.defaultPageable(form)
        );
    }
}
