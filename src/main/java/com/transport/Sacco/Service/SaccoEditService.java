package com.transport.Sacco.Service;

import com.transport.Sacco.Entity.Sacco;
import com.transport.Sacco.Form.RegisterSaccoForm;
import com.transport.Sacco.Repository.SaccoRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.stereotype.Service;

@Service
public class SaccoEditService extends BaseJpaRepoEditService<Sacco, SaccoRepository> {

    public Sacco registerSacco(RegisterSaccoForm form) {
        var sacco = new Sacco();
        sacco.setName(form.getName());
        sacco.setCreatedByEntityId(form.getSessionUserId());

        sacco = save(sacco, form.getSessionUserId());

        return sacco;
    }

    private void checkNameExists(String saccoId, String name) {
        var specification = repository.notDeleted()
                .and(repository.saccoNameIs(name))
                .and(repository.entityIdNot(saccoId));

        boolean exists = repository.exists(specification);

        if (exists) {
            throw new CommonRuntimeException(
                    ExceptionType.BAD_REQUEST,
                    "error.sacco.name.exists"
            );
        }
    }
}
