package com.example.accountsservices.helpers;

import com.example.accountsservices.model.Accounts;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PagingHelper {

    private static final Set<String> fieldNames = new HashSet<>();

    static {
        Accounts accountObj = new Accounts();
        Field[] fields = accountObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        fieldNames.remove("listOfBeneficiary");
        fieldNames.remove("listOfTransactions");
        fieldNames.remove("customer");
    }

    public static Set<String> getAllPageableFieldsOfAccounts() {
        return fieldNames;
    }
}
