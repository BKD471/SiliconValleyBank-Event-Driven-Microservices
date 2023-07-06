package com.example.accountsservices.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValidateNotNull implements ConstraintValidator<NotNullEnum, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(final NotNullEnum annotation) {
        try {
            pattern = Pattern.compile(annotation.regexp());
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Given regex is invalid", e);
        }
    }

    @Override
    public boolean isValid(final Enum<?> value,final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        final Matcher m = pattern.matcher(value.name());
        return m.matches();
    }
}
