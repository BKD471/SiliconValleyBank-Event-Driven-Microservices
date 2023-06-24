//package com.example.accountsservices.validator;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//
//import java.lang.annotation.*;
//
//@Documented
//@Target({ElementType.FIELD,ElementType.TYPE_PARAMETER})
//@Constraint(validatedBy = ValidateAge.class)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ValidAge {
//
//    //default error message
//    String message() default "Your Age should be at Least 18 years old to create an Account";
//
//    //
//    /**
//     * @return the groups the constraint belongs to
//     */
//    Class<?>[] groups() default { };
//
//    /**
//     * @return the payload associated to the constraint
//     */
//    Class<? extends Payload>[] payload() default { };
//}
