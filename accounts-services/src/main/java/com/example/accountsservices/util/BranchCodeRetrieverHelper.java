package com.example.accountsservices.util;

import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.model.Accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.accountsservices.model.Accounts.Branch.*;

public class BranchCodeRetrieverHelper {
    private static final String KOLKATA_UID = "KOL01121997";
    private static final String BANGALORE_UID = "BAN01121997";
    private static final String MUMBAI_UID = "MUM01121997";
    private static final String CHENNAI_UID = "CHE01121997";
    private static final String BARODA_UID = "BAR01121997";
    private static final String HYDERABAD_UID = "HYD01121997";
    private static final String BHUBANESWAR_UID = "BHU01121997";
    private static final String PATNA_UID = "PAT01121997";
    private static final String KERALA_UID = "KER01121997";
    private static final String DELHI_UID = "DEL01121997";

    private static Map<Accounts.Branch, String> getBranchHash() {
        Map<Accounts.Branch, String> hash = new HashMap<>();
        hash.put(KOLKATA, KOLKATA_UID);
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
