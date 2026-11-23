package com.transport.sts.validator;

import com.transport.user.entity.UserStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {RequiredStsUserStatusValidator.class})
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredStsUserStatus {
    String message() default "error.invalid.status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    UserStatus[] statuses();
}
