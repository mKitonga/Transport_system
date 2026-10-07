package com.transport.Authentication.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNumber {
    String message() default "error.user.phone.number.exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
