package com.example.accountsservices.helpers;

public class AllEnumConstantHelpers {
    public  enum UpdateRequest {
        CREATE_ACC,
        ADD_ACCOUNT,
        LEND_LOAN,
        UPDATE_HOME_BRANCH,
        UPDATE_CREDIT_SCORE,
        UPDATE_CUSTOMER_DETAILS,
        UPLOAD_CUSTOMER_IMAGE,
        GET_CUSTOMER_IMAGE,
        GET_CREDIT_SCORE,
        GET_ACC_INFO,
        GET_ALL_ACC,
        BLOCK_ACC,
        CLOSE_ACC,
        RE_OPEN_ACC,
        INC_TRANSFER_LIMIT,
        INC_APPROVED_LOAN_LIMIT,
        DELETE_ACC,
        DELETE_ALL_ACC
    }

    public enum BenUpdateRequest {
        ADD_BEN,
        UPDATE_BEN,
        GET_ALL_BEN,
        GET_BEN,
        DELETE_BEN,
        DELETE_ALL_BEN
    }

    public enum DeleteRequest{
        DELETE_CUSTOMER
    }

    public  enum DIRECTION{
        asc,desc;
    }
    public static DIRECTION ASC=DIRECTION.asc;

    public  enum  AccountType{
        SAVINGS,CURRENT
    }

    public enum Branch{
        KOLKATA,CHENNAI,BANGALORE,HYDERABAD,DELHI,BHUBANESWAR,MUMBAI,BARODA,PATNA,KERALA
    }
    public enum AccountStatus{
        OPEN,BLOCKED,CLOSED
    }

    public static AccountStatus STATUS_BLOCKED=AccountStatus.BLOCKED;
    public static AccountStatus STATUS_CLOSED=AccountStatus.CLOSED;
    public static AccountStatus STATUS_OPENED=AccountStatus.OPEN;

    public enum BanksSupported{
        SBI,AXIS,HDFC,ICICI,CANARA,PNB,ORIENTAL,BOI,YES,BANDHAN,BOB
    }
}
