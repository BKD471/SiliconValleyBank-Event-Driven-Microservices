package com.example.accountsservices.util;

import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.model.Accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.accountsservices.model.Accounts.Branch.*;

public class BranchCodeRetrieverHelper {
    private static String KOLKATA_UID;
    private static String BANGALORE_UID;
    private static String MUMBAI_UID;
    private static String CHENNAI_UID;
    private static String BARODA_UID;
    private static String HYDERABAD_UID;
    private static String BHUBANESWAR_UID;
    private static String PATNA_UID;
    private static String KERALA_UID;
    private static String DELHI_UID;

    static {
        KOLKATA_UID = UUID.randomUUID().toString().split("-")[3];
        BANGALORE_UID = UUID.randomUUID().toString().split("-")[3];
        MUMBAI_UID = UUID.randomUUID().toString().split("-")[3];
        CHENNAI_UID = UUID.randomUUID().toString().split("-")[3];
        BARODA_UID = UUID.randomUUID().toString().split("-")[3];
        HYDERABAD_UID = UUID.randomUUID().toString().split("-")[3];
        BHUBANESWAR_UID = UUID.randomUUID().toString().split("-")[3];
        PATNA_UID = UUID.randomUUID().toString().split("-")[3];
        KERALA_UID = UUID.randomUUID().toString().split("-")[3];
        DELHI_UID = UUID.randomUUID().toString().split("-")[3];
    }

    private static Map<Accounts.Branch, String> getBranchHash() {
        Map<Accounts.Branch, String> hash = new HashMap<>();

        hash.put(KOLKATA,KOLKATA_UID);
        hash.put(BANGALORE, BANGALORE_UID);
        hash.put(MUMBAI, MUMBAI_UID);
        hash.put(CHENNAI, CHENNAI_UID);
        hash.put(BARODA, BARODA_UID);
        hash.put(HYDERABAD, HYDERABAD_UID);
        hash.put(BHUBANESWAR, BHUBANESWAR_UID);
        hash.put(PATNA, PATNA_UID);
        hash.put(KERALA, KERALA_UID);
        hash.put(DELHI, DELHI_UID);
        return hash;
    }

    public static String getBranchCode(Accounts.Branch homeBranch) throws AccountsException {
        String methodName = "getBranchCode(Account.Branch) in BranchCodeHelper";
        if (getBranchHash().containsKey(homeBranch)) return getBranchHash().get(homeBranch);
        throw new AccountsException(String.format("No such" +
                " branches exist with name %s", homeBranch), methodName);

    }
}
