package com.transport.routes.Service;

import com.transport.liby.form.BaseFetchForm;
import com.transport.routes.Entity.Route;
import com.transport.routes.Repository.RouteRepository;
import com.transport.routes.View.RouteView;
import com.transport.sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.sacco.Service.SaccoReadService;
import com.transport.school.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.user.entity.UserType;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteReadService extends BaseJpaRepoReadService<Route, RouteRepository> {

    private SaccoAdminReadService saccoAdminReadService;
    private SaccoReadService saccoReadService;
    private SchoolAdminReadService schoolAdminReadService;

    @Transactional(readOnly = true)
    public boolean isTerminalInUse(String terminalId) {
        return repository.exists(
                repository.notDeleted()
                        .and(repository.terminalEntityIdIs(terminalId))
        );
    }

    @Transactional(readOnly = true)
    public Page<RouteView> listRoutes(BaseFetchForm form, String adminId, UserType userType) {
        var spec = repository.notDeleted();
        if (userType == UserType.SCHOOL_ADMIN) {
            var schoolAdmin = schoolAdminReadService.findActiveAdminByEntityId(adminId)
                    .orElseThrow(this::notFound);
            if (schoolAdmin.getSchool() == null) {
                throw notFound();
            }
            spec = spec.and(repository.schoolEntityIdIs(schoolAdmin.getSchool().getEntityId()));
        } else if (userType == UserType.SACCO_ADMIN) {
            var sacco = saccoAdminReadService.findActiveAdminByEntityId(adminId)
                    .map(admin -> admin.getSacco())
                    .orElseGet(() -> {
                        var ownedSaccos = saccoReadService.findOwnedByAdminId(adminId);
                        return ownedSaccos.size() == 1 ? ownedSaccos.get(0) : null;
                    });
            if (sacco == null) {
                throw notFound();
            }
            spec = spec.and(repository.saccoEntityIdIs(sacco.getEntityId()));
        } else {
            throw notFound();
        }

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        return repository.findAll(spec, repository.defaultPageable(form))
                .map(RouteView::new);
    }

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService saccoAdminReadService) {
        this.saccoAdminReadService = saccoAdminReadService;
    }

    @Autowired
    public void setSaccoReadService(SaccoReadService saccoReadService) {
        this.saccoReadService = saccoReadService;
    }

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService schoolAdminReadService) {
        this.schoolAdminReadService = schoolAdminReadService;
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }
}
