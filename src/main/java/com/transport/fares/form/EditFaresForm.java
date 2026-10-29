package com.transport.fares.form;

import com.transport.fares.entity.DayType;
import com.transport.fares.entity.TimePeriod;
import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EditFaresForm extends SessionUserIdForm {
    @NotBlank
    private String routeId;

    @NotBlank
    private String boardingTerminalId;

    @NotBlank
    private String dropoffTerminalId;

    @NotNull
    private DayType dayType;

    @NotNull
    private TimePeriod  timePeriod;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal fareAmount;
}
