package com.transport.authentication.form;

import com.transport.school.Entity.SchoolGrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterParentForm extends RegisterForm {
    @NotBlank
    private String studentName;

    @NotBlank
    private String admissionNumber;

    @NotNull
    private SchoolGrade grade;

    @NotBlank
    private String schoolEntityId;
}
