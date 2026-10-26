package com.transport.complaints.controller;

import com.transport.complaints.form.SubmitComplaintForm;
import com.transport.complaints.form.ResolveComplaintForm;
import com.transport.complaints.service.ComplaintEditService;
import com.transport.complaints.service.ComplaintReadService;
import com.transport.complaints.view.ComplaintView;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/complaints")
public class ComplaintsController {
    private ComplaintEditService complaintEditService;
    private ComplaintReadService complaintReadService;

    @PostMapping("submit-complaint")
    public EntityApiResponse<ComplaintView> submitComplaint(
            @RequestBody @Valid SubmitComplaintForm form,
            Locale locale) {
        var complaints = complaintEditService.submitComplaint(form);
        return new EntityApiResponse<>(
                Message.get("complaint.submit.success", locale),
                new ComplaintView(complaints)
        );
    }


    @PreAuthorize("hasAuthority('LIST_COMPLAINTS')")
    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public PagedEntityApiResponse<ComplaintView> listComplaints(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());
        var page = complaintReadService.listComplaints(form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('LIST_COMPLAINTS')")
    @GetMapping("list/{complaintId}")
    public EntityApiResponse<ComplaintView> getComplaint(
            @PathVariable String complaintId,
            Authentication auth) {
        var form = new ResolveComplaintForm();
        form.setSessionUserId(auth.getName());
        return new EntityApiResponse<>(complaintReadService.getComplaintForAdmin(complaintId, form));
    }

    @PreAuthorize("hasAuthority('RESOLVE_COMPLAINTS')")
    @PostMapping("resolve-complaint/{complaintId}")
    public EntityApiResponse<ComplaintView> resolveComplaint(
            @PathVariable String complaintId,
            @RequestBody @Valid ResolveComplaintForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var complaint = complaintEditService.resolveComplaint(complaintId, form);
        return new EntityApiResponse<>(
                Message.get("complaint.resolve.success", locale),
                new ComplaintView(complaint)
        );
    }

    @Autowired
    public void setComplaintEditService(ComplaintEditService service) {complaintEditService = service;}

    @Autowired
    public void setComplaintReadService(ComplaintReadService service) {complaintReadService = service;}
}
