package com.example.accountsservices.validator;

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
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value.trim().length()==0) return false;
        LocalDate today=LocalDate.now();
        int years= Period.between(LocalDate.parse(value),today).getYears();
        return years >= 18;
    }
}
