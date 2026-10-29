package com.transport.fares.controller;

import com.transport.fares.form.EditFaresForm;
import com.transport.fares.form.FetchFaresForm;
import com.transport.fares.form.RegisterFaresForm;
import com.transport.fares.service.FareEditService;
import com.transport.fares.service.FareReadService;
import com.transport.fares.view.FareView;
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
@RequestMapping(STS_USER_BASE_URL + "/fares")
public class FaresController {
    private FareEditService editService;
    private FareReadService readService;

    @PreAuthorize("hasAuthority('REGISTER_FARE')")
    @PostMapping("/create")
    public EntityApiResponse<FareView> createFare(
            @RequestBody @Valid RegisterFaresForm form,
            Authentication auth,
            Locale locale) {
        var fare = editService.registerFare(form);
        return new EntityApiResponse<>(
                Message.get("fare.creation.success", locale),
                new FareView(fare)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_FARE')")
    @PutMapping("edit/{fareId}")
    public EntityApiResponse<FareView> editFare(
            @PathVariable String fareId,
            @RequestBody @Valid EditFaresForm form,
            Authentication auth,
            Locale locale) {
        var fare = editService.editFare(fareId, form);
        return new EntityApiResponse<>(
                Message.get("fare.edit.success", locale),
                new FareView(fare));
    }

    @GetMapping("/list")
    public PagedEntityApiResponse<FareView> listFare(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "route", required = false) String route,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new FetchFaresForm();
        form.setQuery(query);
        form.setRouteId(route);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.getSessionUserId();

        var page =  readService.listFares(form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @Autowired
    public void setEditService(FareEditService editService) {this.editService = editService;}

    @Autowired
    public void setReadService(FareReadService readService) {this.readService = readService;}
}
