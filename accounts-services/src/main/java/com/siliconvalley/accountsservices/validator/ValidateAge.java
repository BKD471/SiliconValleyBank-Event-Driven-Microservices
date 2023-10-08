package com.siliconvalley.accountsservices.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class ValidateAge implements ConstraintValidator<ValidAge,String> {
    /**
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(final String value,final ConstraintValidatorContext context) {
        if(value.trim().isEmpty()) return false;
        final LocalDate today=LocalDate.now();
        final int years= Period.between(LocalDate.parse(value),today).getYears();
        return years >= 18;
    }
}
