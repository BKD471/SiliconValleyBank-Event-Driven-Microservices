package com.example.accountsservices.util;

import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.model.Accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.accountsservices.model.Accounts.Branch.*;

public class BranchCodeRetrieverHelper {

    private static Map<Accounts.Branch,String> getBranchHash(){
        Map<Accounts.Branch,String> hash=new HashMap<>();
        hash.put(KOLKATA, UUID.randomUUID().toString());
        hash.put(BANGALORE, UUID.randomUUID().toString());
        hash.put(MUMBAI, UUID.randomUUID().toString());
        hash.put(CHENNAI, UUID.randomUUID().toString());
        hash.put(BARODA, UUID.randomUUID().toString());
        hash.put(HYDERABAD, UUID.randomUUID().toString());
        hash.put(PATNA, UUID.randomUUID().toString());
        hash.put(KERALA, UUID.randomUUID().toString());
        hash.put(DELHI, UUID.randomUUID().toString());
        return hash;
    }

    public  static String getBranchCode(Accounts.Branch homeBranch) throws  AccountsException{
        String methodName="getBranchCode(Account.Branch) in BranchCodeHelper";
        if(getBranchHash().containsKey(homeBranch)) return getBranchHash().get(homeBranch);
        throw  new AccountsException(String.format("No such" +
                " branches exist with name %s",homeBranch),methodName);

    }
}
