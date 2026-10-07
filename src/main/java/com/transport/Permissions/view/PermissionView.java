package com.transport.Permissions.view;

import com.transport.Permissions.entity.Permission;
import com.transport.liby.view.BaseView;

public class PermissionView extends BaseView<Permission> {

    public PermissionView(Permission entity) {
        super(entity);
    }

    public String getDescription(){
        return entity.getDescription();
    }

    public String getPermissionName(){
        return entity.getPermissionName();
    }
}
