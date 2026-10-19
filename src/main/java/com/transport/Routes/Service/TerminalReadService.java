package com.transport.Routes.Service;

import com.transport.Routes.Entity.Terminal;
import com.transport.Routes.Repository.TerminalRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TerminalReadService extends BaseJpaRepoReadService<Terminal, TerminalRepository> {

    public Page<Terminal> listTerminals(BaseFetchForm form) {
        var spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }

    public Terminal findActiveByEntityIdForUpdate(String terminalId) {
        return repository.findActiveByEntityIdForUpdate(terminalId)
                .orElseThrow(() -> new CommonRuntimeException(
                        ExceptionType.NOT_FOUND,
                        "error.entity.not.found"
                ));
    }
}
