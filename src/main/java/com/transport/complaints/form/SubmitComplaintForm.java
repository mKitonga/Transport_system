package com.transport.complaints.form;

import com.transport.complaints.entity.ComplaintService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitComplaintForm {

    @NotNull
    private ComplaintService service;

    @NotBlank
    @Size(max = 2000)
    private String complaintMessage;

    @Size(max = 30)
    @NotBlank
    private String passengerPhoneNumber;

    @Email
    @Size(max = 254)
    private String passengerEmail;

    @Size(max = 36)
    private String routeId;

    @Size(max = 36)
    private String saccoId;

    @Size(max = 36)
    private String schoolId;

    @Size(max = 255)
    private String location;
}
