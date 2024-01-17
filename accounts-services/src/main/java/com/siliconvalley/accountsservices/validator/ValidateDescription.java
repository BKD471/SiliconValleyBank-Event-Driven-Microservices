package com.siliconvalley.accountsservices.validator;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DescriptionType;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.ELECTRICITY;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.SALARY;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.RENT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.E_SHOPPING;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.RECEIVED_FROM_OTHER_ACCOUNT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.CREDIT_CARD_BILL_PAYMENT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.BUSINESS;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.INVESTMENT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.FAMILY;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.OTHERS;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DEPOSIT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DONATION;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DescriptionType.EMI;

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
