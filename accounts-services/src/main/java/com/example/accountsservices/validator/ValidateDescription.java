package com.example.accountsservices.validator;

import com.example.accountsservices.model.Transactions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class ValidateDescription implements ConstraintValidator<ValidDescription, Transactions.DescriptionType> {
    /**
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(Transactions.DescriptionType value, ConstraintValidatorContext context) {
        //null check is already taken care by @notnullEnum
        if(null==value) return true;
        Set<Transactions.DescriptionType> validFields=new HashSet<>();
        validFields.add(Transactions.DescriptionType.ELECTRICITY);
        validFields.add(Transactions.DescriptionType.SALARY);
        validFields.add(Transactions.DescriptionType.DEPOSIT);
        validFields.add(Transactions.DescriptionType.RECEIVED_FROM_OTHER_ACCOUNT);
        validFields.add(Transactions.DescriptionType.EMI);
        validFields.add(Transactions.DescriptionType.CREDIT_CARD_BILL_PAYMENT);
        validFields.add(Transactions.DescriptionType.DONATION);
        validFields.add(Transactions.DescriptionType.RENT);
        validFields.add(Transactions.DescriptionType.E_SHOPPING);
        validFields.add(Transactions.DescriptionType.BUSINESS);
        validFields.add(Transactions.DescriptionType.INVESTMENT);
        validFields.add(Transactions.DescriptionType.FAMILY);
        validFields.add(Transactions.DescriptionType.OTHERS);
        return validFields.contains(value);
    }
}
