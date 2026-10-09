package com.transport.Sacco.Service;

import com.transport.Sacco.Entity.Sacco;
import com.transport.Sacco.Repository.SaccoRepository;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaccoReadService extends BaseJpaRepoReadService<Sacco, SaccoRepository> {

    public List<Sacco> findOwnedByAdminId(String adminId) {
        var spec = repository.notDeleted()
                .and(repository.createdOrUpdatedByEntityIdIs(adminId));
        return repository.findAll(spec);
    }

    public Page<Sacco> listSaccos(BaseFetchForm form) {
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
