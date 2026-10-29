package com.transport.school.View;

import com.transport.school.Entity.School;
import com.transport.liby.view.BaseView;

public class SchoolView extends BaseView<School> {
    public SchoolView(School entity) {super(entity);}

    public String getName() {return entity.getName();}

    public String getLocation() {return entity.getLocation();}
}
