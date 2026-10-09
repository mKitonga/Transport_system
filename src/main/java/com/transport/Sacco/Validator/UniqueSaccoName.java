package com.transport.Sacco.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueSaccoNameValidator.class)
@Target({ElementType.METHOD,  ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSaccoName {
    String message() default "error.sacco.name.exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
