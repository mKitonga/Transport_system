package com.transport.parents.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakeSchoolTripPaymentForm {

    @NotBlank(message = "{error.trip.type.required}")
    private String tripType;

    @NotBlank(message = "{error.trip.id.required}")
    private String tripId;

    @NotBlank(message = "{error.phone.required}")
    @Pattern(regexp = "^(\\+254[17]\\d{8}|0[17]\\d{8})$", message = "{error.phone.invalid}")
    private String phoneNumber;
}
