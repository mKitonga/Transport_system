package com.transport.Routes.Service;

import com.transport.Routes.Entity.Route;
import com.transport.Routes.Form.FetchRoutesForm;
import com.transport.Routes.Repository.RouteRepository;
import com.transport.Routes.View.RouteView;
import com.transport.Sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.Sacco.Service.SaccoReadService;
import com.transport.School.SchoolAdmin.Service.SchoolAdminReadService;
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
    public Page<RouteView> listRoutes(FetchRoutesForm form, String adminId) {
        var spec = repository.notDeleted();
        var saccoAdmin = saccoAdminReadService.findActiveAdminByEntityId(adminId);
        var schoolAdmin = schoolAdminReadService.findActiveAdminByEntityId(adminId);
        var saccoOwner = saccoAdmin.map(admin -> admin.getSacco())
                .orElseGet(() -> {
                    var ownedSaccos = saccoReadService.findOwnedByAdminId(adminId);
                    return ownedSaccos.size() == 1 ? ownedSaccos.get(0) : null;
                });
        var schoolOwner = schoolAdmin.map(admin -> admin.getSchool()).orElse(null);
        boolean ownsSacco = saccoOwner != null;
        boolean ownsSchool = schoolOwner != null;
        if (ownsSacco == ownsSchool) {
            throw notFound();
        }
        if (ownsSacco) {
            spec = spec.and(repository.saccoEntityIdIs(saccoOwner.getEntityId()));
        } else {
            spec = spec.and(repository.schoolEntityIdIs(schoolOwner.getEntityId()));
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
