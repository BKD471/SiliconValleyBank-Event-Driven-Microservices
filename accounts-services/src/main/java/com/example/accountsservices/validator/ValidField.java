package com.example.accountsservices.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER})
@Constraint(validatedBy = ValidateFields.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidField {
    String message() default "The passed field is not an attribute for this resource";

    //

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
