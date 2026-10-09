package com.transport.Routes.Controller;

import com.transport.Routes.Form.EditRouteForm;
import com.transport.Routes.Form.FetchRoutesForm;
import com.transport.Routes.Form.RegisterRouteForm;
import com.transport.Routes.Form.UpdateRouteTerminalsForm;
import com.transport.Routes.Service.RouteEditService;
import com.transport.Routes.Service.RouteReadService;
import com.transport.Routes.View.RouteView;
import com.transport.liby.service.Message;
import com.transport.liby.view.ApiResponse;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/routes")
public class RouteController {

    private RouteEditService routeEditService;
    private RouteReadService routeReadService;

    @PreAuthorize("hasAuthority('REGISTER_ROUTE')")
    @PostMapping("/register")
    public EntityApiResponse<RouteView> registerRoute(
            @RequestBody @Valid RegisterRouteForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var route = routeEditService.registerRoute(form);
        return new EntityApiResponse<>(
                Message.get("route.registration.success", locale),
                new RouteView(route)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_ROUTE')")
    @PutMapping("/edit/{routeId}")
    public EntityApiResponse<RouteView> editRoute(
            @PathVariable("routeId") String routeId,
            @RequestBody @Valid EditRouteForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var route = routeEditService.editRoute(routeId, form, auth.getName());
        return new EntityApiResponse<>(
                Message.get("route.edit.success", locale),
                new RouteView(route)
        );
    }

    @PreAuthorize("hasAuthority('UPDATE_ROUTE_TERMINALS')")
    @PutMapping("/add/{routeId}/terminals")
    public EntityApiResponse<RouteView> updateRouteTerminals(
            @PathVariable("routeId") String routeId,
            @RequestBody @Valid UpdateRouteTerminalsForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var route = routeEditService.updateRouteTerminals(routeId, form, auth.getName());
        return new EntityApiResponse<>(
                Message.get("route.terminals.update.success", locale),
                new RouteView(route)
        );
    }

    @GetMapping("/list")
    public PagedEntityApiResponse<RouteView> listRoutes(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new FetchRoutesForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page = routeReadService.listRoutes(form, auth.getName());
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('DELETE_ROUTE')")
    @DeleteMapping("/delete/{routeId}")
    public ApiResponse deleteRoute(
            @PathVariable("routeId") String routeId,
            Authentication auth,
            Locale locale) {
        routeEditService.deleteRoute(routeId, auth.getName());
        return new ApiResponse(Message.get("route.delete.success", locale));
    }

    @Autowired
    public void setRouteEditService(RouteEditService routeEditService) {
        this.routeEditService = routeEditService;
    }

    @Autowired
    public void setRouteReadService(RouteReadService routeReadService) {
        this.routeReadService = routeReadService;
    }
}
