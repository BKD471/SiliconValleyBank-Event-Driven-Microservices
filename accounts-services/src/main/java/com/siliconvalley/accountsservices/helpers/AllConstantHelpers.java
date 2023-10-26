package com.siliconvalley.accountsservices.helpers;

public class AllConstantHelpers {
    public enum UpdateRequest {
        CREATE_ACC,ADD_ACCOUNT,LEND_LOAN,UPDATE_HOME_BRANCH,UPDATE_CREDIT_SCORE,UPDATE_CUSTOMER_DETAILS,
        UPLOAD_CUSTOMER_IMAGE,GET_CUSTOMER_IMAGE,GET_CREDIT_SCORE,GET_ACC_INFO,GET_ALL_CUSTOMER,GET_ALL_ACC,BLOCK_ACC,
        CLOSE_ACC,RE_OPEN_ACC,INC_TRANSFER_LIMIT,INC_APPROVED_LOAN_LIMIT,DELETE_ACC,DELETE_ALL_ACC}
    public enum BenUpdateRequest {
        ADD_BEN,UPDATE_BEN,GET_ALL_BEN,GET_BEN,DELETE_BEN,DELETE_ALL_BEN}

    public enum DeleteRequest{DELETE_CUSTOMER}

    public  enum  AccountType{
        SAVINGS,CURRENT
    }

    public enum Branch{
        KOLKATA,CHENNAI,BANGALORE,HYDERABAD,DELHI,BHUBANESWAR,MUMBAI,BARODA,PATNA,KERALA
    }
    public enum AccountStatus{
        OPEN,BLOCKED,CLOSED
    }

    public  enum DIRECTION{
        asc,desc
    }

    public enum BanksSupported{
        SBI,AXIS,HDFC,ICICI,CANARA,PNB,ORIENTAL,BOI,YES,BANDHAN,BOB
    }

    public enum RELATION{
        FATHER,MOTHER,SPOUSE,SON,DAUGHTER
    }

    public enum AccountsValidateType {
        UPDATE_CASH_LIMIT,UPDATE_HOME_BRANCH,GENERATE_CREDIT_SCORE,UPDATE_CREDIT_SCORE,
        UPLOAD_PROFILE_IMAGE,CLOSE_ACCOUNT,RE_OPEN_ACCOUNT,BLOCK_ACCOUNT,
        CREATE_ACC, ADD_ACC,GET_ALL_CUSTOMER,GET_ALL_ACC, UPDATE_CUSTOMER_DETAILS
    }

    public enum ValidateField{
        PAN,PASSPORT,ADHAR,EMAIL,VOTER,DRIVING_LICENSE,PHONE,DOB
    }

    public enum  DestinationDtoType{
        AccountsDto,BeneficiaryDto,CustomerDto
    }
    public enum DescriptionType{
        //credit types
        SALARY,DEPOSIT,RECEIVED_FROM_OTHER_ACCOUNT,
        //debit types
        EMI,CREDIT_CARD_BILL_PAYMENT,DONATION,RENT,E_SHOPPING,BUSINESS,
        INVESTMENT,FAMILY,ELECTRICITY,OTHERS
    }

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum validateBenType {
        ADD_BEN, UPDATE_BEN, DELETE_BEN
    }

    public enum ValidateTransactionType{
        GET_PAST_SIX_MONTHS_TRANSACTIONS,GEN_BANK_STATEMENT
    }

    public enum FORMAT_TYPE{
        HTML,PDF,XML
    }

    public static final AccountStatus STATUS_BLOCKED=AccountStatus.BLOCKED;
    public static final  AccountStatus STATUS_CLOSED=AccountStatus.CLOSED;
    public static final AccountStatus STATUS_OPENED=AccountStatus.OPEN;

    public static final RELATION FATHER=RELATION.FATHER;
    public static final RELATION MOTHER=RELATION.MOTHER;
    public  static  final RELATION SPOUSE=RELATION.SPOUSE;


    public static final TransactionType CREDIT=TransactionType.CREDIT;
    public static final TransactionType DEBIT=TransactionType.DEBIT;


    public static final DescriptionType SALARY=DescriptionType.SALARY;
    public static final DescriptionType DEPOSIT=DescriptionType.DEPOSIT;
    public static final DescriptionType RECEIVED_FROM_OTHER_ACCOUNT=DescriptionType.RECEIVED_FROM_OTHER_ACCOUNT;
    public static final DescriptionType CREDIT_CARD_BILL_PAYMENT=DescriptionType.CREDIT_CARD_BILL_PAYMENT;
    public static final DescriptionType DONATION=DescriptionType.DONATION;
    public static final DescriptionType RENT=DescriptionType.RENT;
    public static final DescriptionType E_SHOPPING=DescriptionType.E_SHOPPING;
    public static final DescriptionType BUSINESS=DescriptionType.BUSINESS;
    public static final DescriptionType INVESTMENT=DescriptionType.INVESTMENT;
    public static final DescriptionType FAMILY=DescriptionType.FAMILY;
    public static final DescriptionType ELECTRICITY=DescriptionType.ELECTRICITY;
    public static final DescriptionType OTHERS=DescriptionType.OTHERS;

    public static final AccountsValidateType UPDATE_CASH_LIMIT = AccountsValidateType.UPDATE_CASH_LIMIT;
    public static final AccountsValidateType UPDATE_HOME_BRANCH = AccountsValidateType.UPDATE_HOME_BRANCH;
    public static final AccountsValidateType CLOSE_ACCOUNT = AccountsValidateType.CLOSE_ACCOUNT;
    public static final AccountsValidateType RE_OPEN_ACCOUNT = AccountsValidateType.RE_OPEN_ACCOUNT;
    public static final AccountsValidateType BLOCK_ACCOUNT = AccountsValidateType.BLOCK_ACCOUNT;
    public static final AccountsValidateType CREATE_ACCOUNT = AccountsValidateType.CREATE_ACC;
    public static final AccountsValidateType ADD_ACCOUNT = AccountsValidateType.ADD_ACC;
    public static final AccountsValidateType UPLOAD_PROFILE_IMAGE = AccountsValidateType.UPLOAD_PROFILE_IMAGE;
    public static final ValidateTransactionType GEN_BANK_STATEMENT=ValidateTransactionType.GEN_BANK_STATEMENT;
}
