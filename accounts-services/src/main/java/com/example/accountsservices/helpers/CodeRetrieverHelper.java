package com.example.accountsservices.helpers;

import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;

import java.util.HashMap;
import java.util.Map;

import static com.example.accountsservices.model.Accounts.Branch.*;
import static com.example.accountsservices.model.Accounts.Branch.DELHI;
import static com.example.accountsservices.model.Beneficiary.BanksSupported.*;

public class CodeRetrieverHelper {
    //Bank code
    private static final String SBI_UID = "SBI01121997";
    private static final String AXIS_UID = "AXI01121997";
    private static final String HDFC_UID = "HDF01121997";
    private static final String ICICI_UID = "ICI01121997";
    private static final String CANARA_UID = "CAN01121997";
    private static final String PNB_UID = "PNB01121997";
    private static final String ORIENTAL_UID = "ORI01121997";
    private static final String BOI_UID = "BOI01121997";
    private static final String YES_UID = "YES01121997";
    private static final String BANDHAN_UID = "BAN01121997";
    private static final String BOB_UID = "BOB01121997";

    //Branch code
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

    private static final Map<Beneficiary.BanksSupported, String> hashedBankCode = new HashMap<>();
    private static final Map<Accounts.Branch, String> hashedBranchCode = new HashMap<>();

    static {
        //set bank codes
        hashedBankCode.put(SBI, SBI_UID);
        hashedBankCode.put(AXIS, AXIS_UID);
        hashedBankCode.put(HDFC, HDFC_UID);
        hashedBankCode.put(ICICI, ICICI_UID);
        hashedBankCode.put(CANARA, CANARA_UID);
        hashedBankCode.put(PNB, PNB_UID);
        hashedBankCode.put(ORIENTAL, ORIENTAL_UID);
        hashedBankCode.put(BOI, BOI_UID);
        hashedBankCode.put(YES, YES_UID);
        hashedBankCode.put(BANDHAN, BANDHAN_UID);
        hashedBankCode.put(BOB, BOB_UID);

        //set branch codes
        hashedBranchCode.put(KOLKATA, KOLKATA_UID);
        hashedBranchCode.put(BANGALORE, BANGALORE_UID);
        hashedBranchCode.put(MUMBAI, MUMBAI_UID);
        hashedBranchCode.put(CHENNAI, CHENNAI_UID);
        hashedBranchCode.put(BARODA, BARODA_UID);
        hashedBranchCode.put(HYDERABAD, HYDERABAD_UID);
        hashedBranchCode.put(BHUBANESWAR, BHUBANESWAR_UID);
        hashedBranchCode.put(PATNA, PATNA_UID);
        hashedBranchCode.put(KERALA, KERALA_UID);
        hashedBranchCode.put(DELHI, DELHI_UID);
    }

    public static String getBankCode(Beneficiary.BanksSupported banksSupported) throws AccountsException {
        String methodName = "getBankCode(Account.Branch) in BankCodeRetrieverHelper";
        if (hashedBankCode.containsKey(banksSupported)) return hashedBankCode.get(banksSupported);
        throw new AccountsException(AccountsException.class, String.format("No such" +
                "banks exist with name %s", banksSupported), methodName);

    }

    public static String getBranchCode(Accounts.Branch homeBranch) throws AccountsException {
        String methodName = "getBranchCode(Account.Branch) in BranchCodeHelper";
        if (hashedBranchCode.containsKey(homeBranch)) return hashedBranchCode.get(homeBranch);
        throw new AccountsException(AccountsException.class, String.format("No such" +
                " branches exist with name %s", homeBranch), methodName);

    }
}
