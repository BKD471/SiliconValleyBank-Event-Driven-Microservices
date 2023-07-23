package com.example.accountsservices.validator;

import com.example.accountsservices.helpers.AllConstantHelpers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.example.accountsservices.helpers.AllConstantHelpers.*;
import static com.example.accountsservices.helpers.AllConstantHelpers.DescriptionType.EMI;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ValidateDescription implements ConstraintValidator<ValidDescription, DescriptionType> {
    /**
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(final AllConstantHelpers.DescriptionType value,final ConstraintValidatorContext context) {
        if(Objects.isNull(value)) return false;
        final Set<AllConstantHelpers.DescriptionType> validFields=new HashSet<>();
        validFields.add(ELECTRICITY);
        validFields.add(SALARY);
        validFields.add(DEPOSIT);
        validFields.add(RECEIVED_FROM_OTHER_ACCOUNT);
        validFields.add(EMI);
        validFields.add(CREDIT_CARD_BILL_PAYMENT);
        validFields.add(DONATION);
        validFields.add(RENT);
        validFields.add(E_SHOPPING);
        validFields.add(BUSINESS);
        validFields.add(INVESTMENT);
        validFields.add(FAMILY);
        validFields.add(OTHERS);
        return validFields.contains(value);
    }
}
