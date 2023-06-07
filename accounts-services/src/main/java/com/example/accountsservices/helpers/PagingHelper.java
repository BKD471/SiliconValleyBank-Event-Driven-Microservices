package com.example.accountsservices.helpers;

import java.util.HashSet;
import java.util.Set;

public class PagingHelper {
    public static Set<String> getAllPageableFieldsOfAccounts(){
        Set<String> fields=new HashSet<>();
        fields.add("balance");fields.add("accountType");
        fields.add("branchCode");fields.add("homeBranch");
        fields.add("transferLimitPerDay");fields.add("creditScore");
        fields.add("approvedLoanLimitBasedOnCreditScore");fields.add("totLoanIssuedSoFar");
        fields.add("totalOutStandingAmountPayableToBank");fields.add("accountStatus");
        return fields;
    }
}
