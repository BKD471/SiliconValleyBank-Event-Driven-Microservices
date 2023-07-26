package com.siliconvalley.accountsservices.helpers;

import com.siliconvalley.accountsservices.exception.AccountsException;

import java.util.HashMap;
import java.util.Map;

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
    private static final Map<AllConstantHelpers.BanksSupported,String> hashedBankCode=new HashMap<>();
    private static final Map<AllConstantHelpers.Branch,String> hashedBranchCode=new HashMap<>();
    static {
        hashedBankCode.put(AllConstantHelpers.BanksSupported.SBI, SBI_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.AXIS, AXIS_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.HDFC, HDFC_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.ICICI, ICICI_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.CANARA, CANARA_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.PNB, PNB_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.ORIENTAL, ORIENTAL_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.BOI, BOI_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.YES, YES_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.BANDHAN, BANDHAN_UID);
        hashedBankCode.put(AllConstantHelpers.BanksSupported.BOB, BOB_UID);

        hashedBranchCode.put(AllConstantHelpers.Branch.KOLKATA, KOLKATA_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.BANGALORE, BANGALORE_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.MUMBAI, MUMBAI_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.CHENNAI, CHENNAI_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.BARODA, BARODA_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.HYDERABAD, HYDERABAD_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.BHUBANESWAR, BHUBANESWAR_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.PATNA, PATNA_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.KERALA, KERALA_UID);
        hashedBranchCode.put(AllConstantHelpers.Branch.DELHI, DELHI_UID);
    }

    public static String getBankCode(final AllConstantHelpers.BanksSupported banksSupported) throws AccountsException {
        final String methodName = "getBankCode(Account.Branch) in BankCodeRetrieverHelper";
        if (hashedBankCode.containsKey(banksSupported)) return hashedBankCode.get(banksSupported);
        throw new AccountsException(AccountsException.class, String.format("No such" +
                "banks exist with name %s", banksSupported), methodName);
    }

    public static String getBranchCode(final AllConstantHelpers.Branch homeBranch) throws AccountsException {
        final String methodName = "getBranchCode(Account.Branch) in BranchCodeHelper";
        if (hashedBranchCode.containsKey(homeBranch)) return hashedBranchCode.get(homeBranch);
        throw new AccountsException(AccountsException.class, String.format("No such" +
                " branches exist with name %s", homeBranch), methodName);
    }
}
