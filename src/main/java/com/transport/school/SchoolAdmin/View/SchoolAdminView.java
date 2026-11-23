package com.transport.school.SchoolAdmin.View;

import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.user.view.UserView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolAdminView extends UserView<SchoolAdmin> {

    public SchoolAdminView(SchoolAdmin entity) {
        super(entity);
    }

    public String getSchoolId() {
        return entity.getSchool() != null ? entity.getSchool().getEntityId() : null;
    }

    public String getSchoolName() {
        return entity.getSchool() != null ? entity.getSchool().getName() : null;
    }

    public String getLocation() {
        return entity.getLocation();
    }
}
