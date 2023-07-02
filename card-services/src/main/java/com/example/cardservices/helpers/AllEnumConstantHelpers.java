package com.example.cardservices.helpers;

public class AllEnumConstantHelpers {
    public  enum CardsValidationType{
        ISSUE_CARD,GENERATE_CREDIT_SCORE,REQUEST_FOR_REVISED_CREDIT_LIMIT,FLEXI_PAY,GET_ALL_CARDS
    }

    public static final CardsValidationType ISSUE_CARD=CardsValidationType.ISSUE_CARD;
    public static  final  CardsValidationType GET_ALL_CARDS=CardsValidationType.GET_ALL_CARDS;
    public static final CardsValidationType GENERATE_CREDIT_SCORE=CardsValidationType.GENERATE_CREDIT_SCORE;
    public static final CardsValidationType REQUEST_FOR_REVISED_CREDIT_LIMIT=CardsValidationType.REQUEST_FOR_REVISED_CREDIT_LIMIT;
    public static final CardsValidationType FLEXI_PAY=CardsValidationType.FLEXI_PAY;
}
