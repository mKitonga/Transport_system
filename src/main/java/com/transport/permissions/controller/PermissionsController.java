package com.transport.permissions.controller;

import com.transport.permissions.service.PermissionReadService;
import com.transport.permissions.view.PermissionView;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.view.PagedEntityApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/permissions")
public class PermissionsController {

    private PermissionReadService permissionReadService;

    @GetMapping("/list")
    public PagedEntityApiResponse<PermissionView> listPermissions(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = permissionReadService.listPermissions(form);

        var permissionViews = page.getContent()
                .stream()
                .map(PermissionView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, permissionViews);
    }

    @Autowired
    public void setPermissionReadService(PermissionReadService permissionReadService) {
        this.permissionReadService = permissionReadService;
    }
}
