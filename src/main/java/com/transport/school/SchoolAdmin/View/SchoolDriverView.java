package com.transport.school.SchoolAdmin.View;

import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.user.view.UserView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolDriverView extends UserView<SchoolDriver> {

    public SchoolDriverView(SchoolDriver entity) {
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
