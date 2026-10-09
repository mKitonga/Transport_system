package com.transport.Sacco.Controller;

import com.transport.Sacco.Form.RegisterSaccoForm;
import com.transport.Sacco.Service.SaccoEditService;
import com.transport.Sacco.Service.SaccoReadService;
import com.transport.Sacco.View.SaccoView;
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
@RequestMapping(STS_USER_BASE_URL + "/saccos")
public class SaccoController {

    private  SaccoEditService saccoEditService;
    private  SaccoReadService saccoReadService;

    @PreAuthorize("hasAuthority('REGISTER_SACCO')")
    @PostMapping("/register")
    public EntityApiResponse<SaccoView> registerSacco(
            @RequestBody @Valid RegisterSaccoForm form,
            Authentication auth, Locale locale) {
        form.setSessionUserId(auth.getName());

        var sacco = saccoEditService.registerSacco(form);

        return new EntityApiResponse<>(
                Message.get("sacco.registration.success", locale),
                new SaccoView(sacco)
        );
    }

    @GetMapping("/list")
    public PagedEntityApiResponse<SaccoView> listSaccos(
            @RequestParam(name="query",required = false) String query,
            @RequestParam(name="pageNum",required = false) Integer pageNum,
            @RequestParam(name="pageSIze",required = false) Integer pageSIze
            ) {
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSIze);

        var page = saccoReadService.listSaccos(form);
        var views = page.getContent().stream()
                .map(SaccoView::new)
                .toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setSaccoEditService(SaccoEditService saccoEditService) {
        this.saccoEditService = saccoEditService;
    }

    @Autowired
    public void setSaccoReadService(SaccoReadService saccoReadService) {
        this.saccoReadService = saccoReadService;
    }
}
