package com.transport.complaints.service;

import com.transport.Routes.Service.RouteReadService;
import com.transport.Sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.Sacco.Service.SaccoReadService;
import com.transport.School.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.School.Service.SchoolReadService;
import com.transport.complaints.entity.ComplaintService;
import com.transport.complaints.entity.ComplaintStatus;
import com.transport.complaints.entity.Complaints;
import com.transport.complaints.form.ResolveComplaintForm;
import com.transport.complaints.form.SubmitComplaintForm;
import com.transport.complaints.repository.ComplaintsRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ComplaintEditService extends BaseJpaRepoEditService<Complaints, ComplaintsRepository> {

    private RouteReadService routeReadService;
    private SaccoReadService saccoReadService;
    private SchoolReadService schoolReadService;
    private SaccoAdminReadService saccoAdminReadService;
    private SchoolAdminReadService schoolAdminReadService;

    @Transactional
    public Complaints submitComplaint(SubmitComplaintForm form) {
        var complaint = new Complaints();

        complaint.setService(form.getService());
        complaint.setComplaintMessage(form.getComplaintMessage().trim());
        complaint.setPassengerPhoneNumber(form.getPassengerPhoneNumber().trim());
        complaint.setPassengerEmail(trimToNull(form.getPassengerEmail()));
        complaint.setStatus(ComplaintStatus.OPEN);

        if (form.getService() == ComplaintService.SACCO) {
            if (isBlank(form.getSaccoId()) || !isBlank(form.getSchoolId())) {
                throw badRequest("error.complaint.service.details.invalid");
            }
            var sacco = saccoReadService.findByEntityId(form.getSaccoId());
            complaint.setSacco(sacco);

            if (!isBlank(form.getRouteId())) {
                var route = routeReadService.findByEntityId(form.getRouteId());
                if (route.getSacco() == null || !route.getSacco().getEntityId().equals(sacco.getEntityId())) {
                    throw notFound();
                }
                complaint.setRoute(route);
            }
        } else if (form.getService() == ComplaintService.SCHOOL) {
            if (isBlank(form.getSchoolId()) || !isBlank(form.getSaccoId())) {
                throw badRequest("error.complaint.service.details.invalid");
            }

            var school = schoolReadService.findByEntityId(form.getSchoolId());
            if (!isBlank(form.getLocation()) && !form.getLocation().trim().equalsIgnoreCase(school.getLocation())) {
                throw badRequest("error.complaint.location.mismatch");
            }
            complaint.setSchool(school);

            if (!isBlank(form.getRouteId())) {
                var route = routeReadService.findByEntityId(form.getRouteId());
                if (route.getSchool() == null || !route.getSchool().getEntityId().equals(school.getEntityId())) {
                    throw notFound();
                }
                complaint.setRoute(route);
            }
        } else {
            throw badRequest("error.complaint.service.invalid");
        }
        return save(complaint);
    }

    @Transactional
    public Complaints resolveComplaint(String complaintId, ResolveComplaintForm form) {
        var complaint = findByEntityId(complaintId);

        if (complaint.getStatus() == ComplaintStatus.RESOLVED) {
            throw badRequest("error.complaint.already.resolved");
        }

        String sessionUserId = form.getSessionUserId();
        validateAdminOwnsComplaint(complaint, sessionUserId);

        complaint.setResponseMessage(form.getResponseMessage().trim());
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setRespondedByEntityId(sessionUserId);
        complaint.setRespondedAt(LocalDateTime.now());

        return save(complaint, sessionUserId);
    }

    private void validateAdminOwnsComplaint(Complaints complaint, String adminId) {
        if (isBlank(adminId)) {
            throw notFound();
        }

        if (complaint.getService() == ComplaintService.SACCO) {
            var admin = saccoAdminReadService.findActiveAdminByEntityId(adminId)
                    .orElseThrow(this::notFound);

            if (complaint.getSacco() == null || admin.getSacco() == null || !complaint.getSacco().getEntityId().equals(admin.getSacco().getEntityId())) {
                throw notFound();
            }
            return;
        }

        if (complaint.getService() == ComplaintService.SCHOOL) {
            var admin = schoolAdminReadService.findActiveAdminByEntityId(adminId)
                    .orElseThrow(this::notFound);

            if (complaint.getSchool() == null || admin.getSchool() == null || !complaint.getSchool().getEntityId().equals(admin.getSchool().getEntityId())) {
                throw notFound();
            }
            return;
        }
        throw notFound();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String trimToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(
                ExceptionType.NOT_FOUND,
                "error.entity.not.found"
        );
    }

    private CommonRuntimeException badRequest(String message) {
        return new CommonRuntimeException(ExceptionType.BAD_REQUEST, message);
    }

    @Autowired
    public void setRouteReadService(RouteReadService service) {routeReadService = service;}

    @Autowired
    public void setSaccoReadService(SaccoReadService service) {saccoReadService = service;}

    @Autowired
    public void setSchoolReadService(SchoolReadService service) {schoolReadService = service;}

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService service) {saccoAdminReadService = service;}

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService service) {schoolAdminReadService = service;}
}
