package com.transport.Trips.form;

import com.transport.Trips.entity.TripType;
import com.transport.School.Entity.SchoolGrade;
import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EditTripForm extends SessionUserIdForm {

    @NotNull
    private TripType tripType;

    private SchoolGrade grade;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
