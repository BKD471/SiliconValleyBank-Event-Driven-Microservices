package com.example.accountsservices.validator;

import com.example.accountsservices.model.Accounts;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class ValidateFields implements ConstraintValidator<ValidField,String> {
    /**
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Set<String> fields=new HashSet<>();
        fields.add("balance");fields.add("accountType");
        fields.add("branchCode");fields.add("homeBranch");
        fields.add("transferLimitPerDay");fields.add("creditScore");
        fields.add("approvedLoanLimitBasedOnCreditScore");fields.add("totLoanIssuedSoFar");
        fields.add("totalOutStandingAmountPayableToBank");fields.add("accountStatus");

        if(!fields.contains(value)) return false;
        return true;
    }
}
