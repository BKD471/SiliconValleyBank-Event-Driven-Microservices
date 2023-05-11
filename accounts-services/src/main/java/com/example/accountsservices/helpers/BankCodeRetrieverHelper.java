package com.example.accountsservices.helpers;

import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;

import java.util.HashMap;
import java.util.Map;

import static com.example.accountsservices.model.Accounts.Branch.*;
import static com.example.accountsservices.model.Accounts.Branch.DELHI;
import static com.example.accountsservices.model.Beneficiary.BanksSupported.*;

public class BankCodeRetrieverHelper {

    private static final String SBI_UID = "SBI01121997";
    private static final String AXIS_UID = "AXI01121997";
    private static final String HDFC_UID = "HDF01121997";
    private static final String ICICI_UID= "ICI01121997";
    private static final String CANARA_UID= "CAN01121997";
    private static final String PNB_UID = "PNB01121997";
    private static final String ORIENTAL_UID = "ORI01121997";
    private static final String BOI_UID = "BOI01121997";
    private static final String YES_UID = "YES01121997";
    private static final String BANDHAN_UID = "BAN01121997";
    private static final String BOB_UID="BOB01121997";

    private static Map<Beneficiary.BanksSupported, String> getBankCode() {
        Map<Beneficiary.BanksSupported, String> hash = new HashMap<>();
        hash.put(SBI, SBI_UID);
        hash.put(AXIS, AXIS_UID);
        hash.put(HDFC, HDFC_UID);
        hash.put(ICICI, ICICI_UID);
        hash.put(CANARA, CANARA_UID);
        hash.put(PNB, PNB_UID);
        hash.put(ORIENTAL, ORIENTAL_UID);
        hash.put(BOI, BOI_UID);
        hash.put(YES, YES_UID);
        hash.put(BANDHAN, BANDHAN_UID);
        hash.put(BOB,BOB_UID);
        return hash;
    }

    public static String getBankCode(Beneficiary.BanksSupported banksSupported) throws AccountsException {
        String methodName = "getBankCode(Account.Branch) in BankCodeRetrieverHelper";
        if (getBankCode().containsKey(banksSupported)) return getBankCode().get(banksSupported);
        throw new AccountsException(AccountsException.class,String.format("No such" +
                "banks exist with name %s", banksSupported), methodName);

    }
}
