package com.siliconvalley.loansservices.helpers;

public class AllConstantsHelper {
    public enum LoansValidateType{
        ISSUE_LOAN,PAY_EMI,GET_INFO_LOAN,GET_ALL_LOAN
    }

    public static final LoansValidateType ISSUE_LOAN=LoansValidateType.ISSUE_LOAN;
    public static final LoansValidateType PAY_EMI=LoansValidateType.PAY_EMI;
    public static final LoansValidateType GET_INFO_LOAN=LoansValidateType.GET_INFO_LOAN;
    public static final LoansValidateType GET_ALL_LOAN=LoansValidateType.GET_ALL_LOAN;
}
