package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.BankStatement;
import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.convertToUtilDate;


@Slf4j
@Service("jasperPdfService")
public class PdfServiceJasperImpl extends AbstractService implements IPdfService {
    private static final String PATH_TO_PROPERTIES_FILE="C:\\Users\\Bhaskar\\Desktop\\Spring\\Banks Services\\accounts-services\\src\\main\\java\\com\\siliconvalley\\accountsservices\\service\\properties\\PdfServiceJasper.properties";
    private static final Map<String,Object> params=new HashMap<>();
    private static final Properties properties=new Properties();
    private final String PATH_TO_JASPER_XML;
    private final String PATH_TO_DOWNLOADABLES;
    static {
        params.put("companyName",companyName);
        params.put("city",city);
        params.put("street",street);
        params.put("ZipCode",ZipCode);
        params.put("faxNumber",faxNumber);
        params.put("State",State);
        params.put("country",country);

        try{
            properties.load(new FileReader(PATH_TO_PROPERTIES_FILE));
        }catch (IOException e){
            log.error("Error while Reading properties file of JasperPdfService");
        }
    }

    protected PdfServiceJasperImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
        this.PATH_TO_JASPER_XML=properties.getProperty("path.jrxml");
        this.PATH_TO_DOWNLOADABLES=properties.getProperty("path.downloadables");
    }

    /**
     * @param startDate
     * @param endDate
     * @param accountNumber
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public ByteArrayInputStream generateBankStatement(LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException {
        return null;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String generateBankStatement(String accountNumber,LocalDate startDate, LocalDate endDate) throws JRException {
        log.info("################# Pdf Creation Service started ###################################");

        List<Transactions> transactionsListBetweenDate = prepareTransactionsListBetweenDate(startDate, endDate, accountNumber);
        Accounts loadAccount = fetchAccountByAccountNumber(accountNumber);

        BankStatement bankStatement = BankStatement.builder()
                .accountName(loadAccount.getCustomer().getName())
                .accountNumber(loadAccount.getAccountNumber())
                .accountType(loadAccount.getAccountType().toString())
                .branch(loadAccount.getHomeBranch().toString())
                .rateOfInterest(loadAccount.getRateOfInterest())
                .balance(loadAccount.getBalance())
                .date(new Date())
                .listOfTransaction(transactionsListBetweenDate)
                .build();

        params.put("accountName",bankStatement.getAccountName());
        params.put("accountNumber",bankStatement.getAccountNumber());
        params.put("branch",bankStatement.getBranch());
        params.put("accountType",bankStatement.getAccountType());
        params.put("rateOfInterest",bankStatement.getRateOfInterest());
        params.put("balance",bankStatement.getBalance());
        params.put("date",bankStatement.getDate());
        params.put("startDate", convertToUtilDate(startDate));
        params.put("endDate",convertToUtilDate(endDate));

        //transactionTimeStamp   transactionId   transactionAmount
        // transactedAccountNumber  transactionType  description  balance
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(transactionsListBetweenDate);
        JasperReport report= JasperCompileManager.compileReport(PATH_TO_JASPER_XML);
        JasperPrint print= JasperFillManager.fillReport(report,params,dataSource);
        JasperExportManager.exportReportToPdfFile(print,PATH_TO_DOWNLOADABLES);
        return "Report Successfully generated";
    }
}
