package com.example.accountsservices.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD,ElementType.TYPE_PARAMETER})
@Constraint(validatedBy = ValidateDescription.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDescription {
    //default error message
    String message() default "Please mention a valid description";

    //
    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default { };

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default { };
}
