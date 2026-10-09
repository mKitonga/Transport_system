package com.transport.Routes.Controller;

import com.transport.Routes.Form.EditTerminalForm;
import com.transport.Routes.Form.RegisterTerminalForm;
import com.transport.Routes.Service.TerminalEditService;
import com.transport.Routes.Service.TerminalReadService;
import com.transport.Routes.View.TerminalView;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.ApiResponse;
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
@RequestMapping(STS_USER_BASE_URL + "/terminals")
public class TerminalController {
    private TerminalEditService terminalEditService;
    private TerminalReadService terminalReadService;

    @PreAuthorize("hasAuthority('REGISTER_TERMINAL')")
    @PostMapping("/register")
    public EntityApiResponse<TerminalView> registerTerminal(
            @RequestBody @Valid RegisterTerminalForm form,
            Authentication auth, Locale locale) {
        form.setSessionUserId(auth.getName());

        var terminal = terminalEditService.registerTerminal(form);

        return new EntityApiResponse<>(
                Message.get("terminal.registration.success", locale),
                new TerminalView(terminal)
        );
    }


    @PreAuthorize("hasAuthority('EDIT_TERMINAL')")
    @PutMapping("/edit/{terminalId}")
    public EntityApiResponse<TerminalView> editTerminal(
            @RequestBody @Valid EditTerminalForm form,
            Authentication auth, Locale locale,
            @PathVariable String terminalId) {
        form.setSessionUserId(auth.getName());

        var terminal = terminalEditService.editTerminal(terminalId, form);

        return new EntityApiResponse<>(
                Message.get("terminal.edit.success", locale),
                new TerminalView(terminal)
        );
    }

    @GetMapping("/list")
    public PagedEntityApiResponse<TerminalView> listTerminals(
            @RequestParam(name="query", required = false) String query,
            @RequestParam(name="pageNum", required =  false) Integer pageNum,
            @RequestParam(name="pageSize", required = false) Integer pageSize){
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page =  terminalReadService.listTerminals(form);
        var views = page.getContent().stream()
                .map(TerminalView::new)
                .toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('DELETE_TERMINAL')")
    @DeleteMapping("/delete/{terminalId}")
    public ApiResponse deleteTerminal(
            Authentication auth,
            Locale locale,
            @PathVariable String terminalId) {
        terminalEditService.deleteTerminal(terminalId, auth.getName());
        return new ApiResponse(Message.get("terminal.delete.success", locale));
    }


    @Autowired
    public void setTerminalEditService(TerminalEditService terminalEditService) {
        this.terminalEditService = terminalEditService;
    }

    @Autowired
    public void setTerminalReadService(TerminalReadService terminalReadService) {
        this.terminalReadService = terminalReadService;
    }
}
