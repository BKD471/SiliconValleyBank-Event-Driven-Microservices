package com.example.accountsservices.validator;

import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

import static com.example.accountsservices.helpers.AllEnumConstantHelpers.*;
import static com.example.accountsservices.helpers.AllEnumConstantHelpers.DescriptionType.EMI;

public class ValidateDescription implements ConstraintValidator<ValidDescription, DescriptionType> {
    /**
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(AllEnumConstantHelpers.DescriptionType value, ConstraintValidatorContext context) {
        //null check is already taken care by @notnullEnum
        if(null==value) return true;
        Set<AllEnumConstantHelpers.DescriptionType> validFields=new HashSet<>();
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
