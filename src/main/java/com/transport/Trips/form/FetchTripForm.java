package com.transport.Trips.form;

import com.transport.School.Entity.SchoolGrade;
import com.transport.Trips.entity.TripStatus;
import com.transport.Trips.entity.TripType;
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
