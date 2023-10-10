package com.siliconvalley.accountsservices.helpers;

import com.siliconvalley.accountsservices.exception.AccountsException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@Slf4j
public class CodeRetrieverHelper {
    private static final String PATH_TO_PROPERTIES_FILE = "accounts-services/src/main/java/com/siliconvalley/accountsservices/properties/helper_properties/CodeRetriever.properties";
    private static final String CLASS_NAME = CodeRetrieverHelper.class.getSimpleName();
    private static final Properties properties = new Properties();
    private static final Map<AllConstantHelpers.BanksSupported, String> hashedBankCode = new HashMap<>();
    private static final Map<AllConstantHelpers.Branch, String> hashedBranchCode = new HashMap<>();
    private static Set<String> listOfBankCodes;
    private static Set<String> listOfBranchCodes;

    static {
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
        } catch (IOException e) {
            log.error("Error while reading {}'s properties file {}", CLASS_NAME, e.getMessage());
        }
    }

    static {
        listOfBankCodes = new HashSet<>(Arrays.stream(properties.getProperty("bankCodes").split(",")).toList());
        listOfBranchCodes = new HashSet<>(Arrays.stream(properties.getProperty("branchCodes").split(",")).toList());
        listOfBankCodes.forEach(bankCodes -> {
            String[] parsedDataArray = bankCodes.split(":");
            String bankName = parsedDataArray[0];
            String bankID = parsedDataArray[1];
            hashedBankCode.put(AllConstantHelpers.BanksSupported.valueOf(bankName), bankID);
        });

        listOfBranchCodes.forEach(brnachCodes -> {
            String[] parsedDataArray = brnachCodes.split(":");
            String branchName = parsedDataArray[0];
            String branchID = parsedDataArray[1];
            hashedBranchCode.put(AllConstantHelpers.Branch.valueOf(branchName), branchID);
        });

    }

    public static String getBankCode(final AllConstantHelpers.BanksSupported banksSupported) throws AccountsException {
        final String methodName = "getBankCode(Account.Branch) in CodeRetrieverHelper";
        if (hashedBankCode.containsKey(banksSupported)) return hashedBankCode.get(banksSupported);
        throw new AccountsException(AccountsException.class, String.format("No such" +
                "banks exist with name %s", banksSupported), methodName);
    }

    public static String getBranchCode(final AllConstantHelpers.Branch homeBranch) throws AccountsException {
        final String methodName = "getBranchCode(Account.Branch) in CodeRetrieverHelper";
        if (hashedBranchCode.containsKey(homeBranch)) return hashedBranchCode.get(homeBranch);
        throw new AccountsException(AccountsException.class, String.format("No such" +
                " branches exist with name %s", homeBranch), methodName);
    }
}
