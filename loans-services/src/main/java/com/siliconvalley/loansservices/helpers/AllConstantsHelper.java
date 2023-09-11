package com.siliconvalley.loansservices.helpers;

public class AllConstantsHelper {
    public enum LoansValidateType{
        ISSUE_LOAN,PAY_EMI,GET_INFO_LOAN,GET_ALL_LOAN,DRIVER_METHOD_VALIDATION
    }

    public enum RequestType{
        BORROW_LOAN,PAY_INSTALLMENTS,GET_INFO,GET_ALL_LOANS_FOR_CUSTOMER,DOWNLOAD_EMI_STMT
    }

    public enum LoanType{
        HOUSE_LOAN,CAR_LOAN,EDUCATION_LOAN,BUSINESS_LOAN
    }
    public static final RequestType BORROW_LOAN=RequestType.BORROW_LOAN;
    public static final RequestType PAY_INSTALLMENTS=RequestType.PAY_INSTALLMENTS;
    public static final RequestType GET_INFO=RequestType.GET_INFO;
    public  static  final RequestType GET_ALL_LOANS_BY_CUST=RequestType.GET_ALL_LOANS_FOR_CUSTOMER;
    public static final LoansValidateType ISSUE_LOAN=LoansValidateType.ISSUE_LOAN;
    public static final LoansValidateType DRIVER_METHOD_VALIDATION=LoansValidateType.DRIVER_METHOD_VALIDATION;
    public static final LoansValidateType PAY_EMI=LoansValidateType.PAY_EMI;
    public static final LoansValidateType GET_INFO_LOAN=LoansValidateType.GET_INFO_LOAN;
    public static final LoansValidateType GET_ALL_LOAN=LoansValidateType.GET_ALL_LOAN;
}
