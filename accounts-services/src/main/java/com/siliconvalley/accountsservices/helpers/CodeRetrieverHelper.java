package com.siliconvalley.accountsservices.helpers;

import com.siliconvalley.accountsservices.exception.AccountsException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@Slf4j
@Component
public class CodeRetrieverHelper {
    private static final Map<AllConstantHelpers.BanksSupported, String> hashedBankCode = new HashMap<>();
    private static final Map<AllConstantHelpers.Branch, String> hashedBranchCode = new HashMap<>();
    private static Set<String> listOfBankCodes;
    private static Set<String> listOfBranchCodes;


   CodeRetrieverHelper(@Value("${path.helper.code}") String path_properties){
       Properties properties = new Properties();
       try {
           properties.load(new FileInputStream(path_properties));
       } catch (IOException e) {
           log.error("Error while reading {}'s properties file {}", this.getClass().getSimpleName(), e.getMessage());
       }
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
