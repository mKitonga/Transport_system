package com.transport.complaints.service;

import com.transport.sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.school.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.complaints.entity.ComplaintService;
import com.transport.complaints.entity.Complaints;
import com.transport.complaints.form.ResolveComplaintForm;
import com.transport.complaints.repository.ComplaintsRepository;
import com.transport.complaints.view.ComplaintView;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintReadService extends BaseJpaRepoReadService<Complaints, ComplaintsRepository> {
    private SaccoAdminReadService saccoAdminReadService;
    private SchoolAdminReadService schoolAdminReadService;

    @Transactional(readOnly = true)
    public Page<ComplaintView> listComplaints(BaseFetchForm form) {
        var spec = repository.notDeleted();
        String sessionUserId = form.getSessionUserId();

        var saccoAdmin = saccoAdminReadService.findActiveAdminByEntityId(sessionUserId).orElse(null);

        if (saccoAdmin != null && saccoAdmin.getSacco() != null) {
            spec = spec.and(repository.serviceIs(ComplaintService.SACCO))
                    .and(repository.saccoEntityIdIs(
                            saccoAdmin.getSacco().getEntityId()
                    ));
        } else {
            var schoolAdmin = schoolAdminReadService.findActiveAdminByEntityId(sessionUserId).orElseThrow(this::notFound);

            if (schoolAdmin.getSchool() == null) {
                throw notFound();
            }

            spec = spec.and(repository.serviceIs(ComplaintService.SCHOOL))
                    .and(repository.schoolEntityIdIs(schoolAdmin.getSchool().getEntityId()));
        }

        if (form.getQuery() != null && !form.getQuery().isBlank()) {
            spec = spec.and(repository.searchLike(form.getQuery()));
        }
        return repository.findAll(spec, repository.defaultPageable(form))
                .map(ComplaintView::new);
    }
    
    @Transactional(readOnly = true)
    public ComplaintView getComplaintForAdmin(String complaintId, ResolveComplaintForm form) {
        var complaint = findByEntityId(complaintId);
        String sessionUserId = form.getSessionUserId();

        if (complaint.getService() == ComplaintService.SACCO) {
            var admin = saccoAdminReadService.findActiveAdminByEntityId(sessionUserId)
                    .orElseThrow(this::notFound);

            if (complaint.getSacco() == null || admin.getSacco() == null || !complaint.getSacco().getEntityId().equals(admin.getSacco().getEntityId())) {
                throw notFound();
            }
        } else if (complaint.getService() == ComplaintService.SCHOOL) {
            var admin = schoolAdminReadService
                    .findActiveAdminByEntityId(sessionUserId)
                    .orElseThrow(this::notFound);

            if (complaint.getSchool() == null || admin.getSchool() == null || !complaint.getSchool().getEntityId().equals(admin.getSchool().getEntityId())) {
                throw notFound();
            }
        } else {
            throw notFound();
        }
        return new ComplaintView(complaint);
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService service) {saccoAdminReadService = service;}

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService service) {schoolAdminReadService = service;}
}
