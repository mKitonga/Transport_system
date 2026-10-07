package com.transport.Authentication.Validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VerificationCodeExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface VerificationCodeExists {
    String message() default "Entity not found.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
