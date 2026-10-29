package com.transport.trips.form;

import com.transport.school.Entity.SchoolGrade;
import com.transport.trips.entity.TripStatus;
import com.transport.trips.entity.TripType;
import com.transport.liby.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchTripForm extends BaseFetchForm {
    private TripType tripType;
    private TripStatus tripStatus;
    private SchoolGrade schoolGrade;
}
