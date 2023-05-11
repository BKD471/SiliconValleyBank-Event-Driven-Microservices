package com.example.accountsservices.helpers;

public class ResponseTypes {
    public enum ResponsesType{
        GET,POST,PUT,DELETE
    }

    public static final ResponsesType GET=ResponsesType.GET;
    public static final ResponsesType POST=ResponsesType.POST;
    public static final ResponsesType PUT=ResponsesType.PUT;
    public static final ResponsesType DELETE=ResponsesType.DELETE;
}
