package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.helpers.TransactionsInvoicableObject;
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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.convertToUtilDate;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToTransactionsInvoicableObject;


@Slf4j
@Service("jasperPdfService")
public class PdfServiceJasperImpl extends AbstractService implements IPdfService {
    private static final String PATH_TO_PROPERTIES_FILE="accounts-services/src/main/java/com/siliconvalley/accountsservices/service/properties/PdfServiceJasper.properties";
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
            properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
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
     * @param reportFormat
     * @param startDate
     * @param endDate
     * @param accountNumber
     * @return
     * @throws FileNotFoundException
     * @throws JRException
     */
    @Override
    public String generateBankStatement(BankStatementRequestDto.FORMAT_TYPE reportFormat, LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException, JRException {
        log.info("################# Pdf Creation Service started ###################################");
        Set<Transactions> transactionsListBetweenDate =prepareTransactionsSetBetweenDate(startDate,endDate,accountNumber);
        Accounts loadAccount = fetchAccountByAccountNumber(accountNumber);

        BankStatement bankStatement = BankStatement.builder()
                .accountName(loadAccount.getCustomer().getName())
                .accountNumber(loadAccount.getAccountNumber())
                .accountType(loadAccount.getAccountType().toString())
                .branch(loadAccount.getHomeBranch().toString())
                .rateOfInterest(loadAccount.getRateOfInterest())
                .balance(loadAccount.getBalance())
                .listOfTransaction(loadAccount.getListOfTransactions())
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


        List<TransactionsInvoicableObject> listOfTransactionsBetweenDate=
                transactionsListBetweenDate.stream().map(MapperHelper::mapToTransactionsInvoicableObject).toList();

        //transactionTimeStamp   transactionId   transactionAmount
        // transactedAccountNumber  transactionType  description  balance
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(listOfTransactionsBetweenDate);
        JasperReport report= JasperCompileManager.compileReport(PATH_TO_JASPER_XML);
        JasperPrint print= JasperFillManager.fillReport(report,params,dataSource);
        switch (reportFormat){
            case PDF -> JasperExportManager.exportReportToPdfFile(print,PATH_TO_DOWNLOADABLES);
            case HTML -> JasperExportManager.exportReportToHtmlFile(print,PATH_TO_DOWNLOADABLES);
            case XML -> JasperExportManager.exportReportToXmlFile(print,PATH_TO_DOWNLOADABLES,false);
        }

        return "Report Successfully generated";
    }

}
