package com.transport.parents.view;

import com.transport.parents.entity.Parent;
import com.transport.school.Entity.SchoolGrade;
import com.transport.user.view.UserView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentView extends UserView<Parent> {

    public ParentView(Parent entity) {
        super(entity);
    }

    public String getStudentName() {
        return entity.getStudentName();
    }

    public String getAdmissionNumber() {
        return entity.getAdmissionNumber();
    }

    public SchoolGrade getGrade() {
        return entity.getGrade();
    }

    public String getSchoolId() {
        return entity.getSchool() != null ? entity.getSchool().getEntityId() : null;
    }

    public String getSchoolName() {
        return entity.getSchoolName();
    }

    public String getSchoolLocation() {
        return entity.getSchoolLocation();
    }
}
