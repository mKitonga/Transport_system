package com.transport.School.Controller;

import com.transport.School.Form.RegisterSchoolForm;
import com.transport.School.Service.SchoolEditService;
import com.transport.School.Service.SchoolReadService;
import com.transport.School.View.SchoolView;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/schools")
public class SchoolController {
    private SchoolEditService schoolEditService;
    private SchoolReadService  schoolReadService;

    @PreAuthorize("hasAuthority('REGISTER_SCHOOL')")
    @PostMapping("register")
    public EntityApiResponse<SchoolView> registerSchool(
            @RequestBody @Valid RegisterSchoolForm form,
            Authentication auth, Locale locale) {
        form.setSessionUserId(auth.getName());

        var school = schoolEditService.registerSchool(form);

        return new EntityApiResponse<>(
                Message.get("school.registration.success", locale),
                new SchoolView(school)
        );
    }

    @GetMapping("list")
    public PagedEntityApiResponse<SchoolView> listSchools(
            @RequestParam(name="query",required = false) String query,
            @RequestParam(name="pageNum",required = false) Integer pageNum,
            @RequestParam(name="pageSize",required = false) Integer pageSize
    ) {
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = schoolReadService.listSchools(form);
        var views = page.getContent().stream()
                .map(SchoolView::new)
                .toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setSchoolEditService(SchoolEditService schoolEditService) {
        this.schoolEditService = schoolEditService;
    }

    @Autowired
    public void setSchoolReadService(SchoolReadService schoolReadService) {
        this.schoolReadService = schoolReadService;
    }
}
