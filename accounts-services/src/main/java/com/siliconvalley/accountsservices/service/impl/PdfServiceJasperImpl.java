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
import com.siliconvalley.accountsservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.*;


@Slf4j
@Service("jasperPdfService")
public class PdfServiceJasperImpl extends AbstractService implements IPdfService {
    private static final Map<String,Object> params=new HashMap<>();
    private final IValidationService validationService;
    private final String PATH_TO_JASPER_XML;
    private  final String PATH_TO_DOWNLOADABLES;
    private  String PATH_TO_DOWNLOADABLES_PDF;
    private  String PATH_TO_DOWNLOADABLES_HTML;
    private  String PATH_TO_DOWNLOADABLES_XML;
    private final Set<String> companydetailsSet;


     PdfServiceJasperImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository,
                                   IValidationService validationService,
                                   @Value("${path.service.pdf}") String path_to_pdf_properties,
                                   @Value("${path.details.company}") String path_to_company_details_properties) {
        super(accountsRepository, customerRepository);

        Properties properties1 = new Properties();
        Properties properties2 = new Properties();
        try{
            properties1.load(new FileInputStream(path_to_pdf_properties));
            properties2.load(new FileInputStream(path_to_company_details_properties));
        }catch (IOException e){
            log.error("Error while reading {}'s properties file {}",this.getClass().getSimpleName(),e.getMessage());
        }
        this.validationService=validationService;
        this.PATH_TO_JASPER_XML= properties1.getProperty("path.jrxml");
        this.PATH_TO_DOWNLOADABLES= properties1.getProperty("path.downloadables");
        this.companydetailsSet= new HashSet<>(Arrays.stream(properties2.getProperty("companyDetails").split(",")).toList());

        companydetailsSet.forEach( companyDetails->{
            String[] parsedData=companyDetails.split(":");
            String field=parsedData[0];
            String value=parsedData[1];
            params.put(field,value);
        });
        this.PATH_TO_DOWNLOADABLES_XML=PATH_TO_DOWNLOADABLES;
        this.PATH_TO_DOWNLOADABLES_PDF=PATH_TO_DOWNLOADABLES;
        this.PATH_TO_DOWNLOADABLES_HTML=PATH_TO_DOWNLOADABLES;
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


    private void reset(){
        PATH_TO_DOWNLOADABLES_PDF=this.PATH_TO_DOWNLOADABLES;
        PATH_TO_DOWNLOADABLES_HTML=this.PATH_TO_DOWNLOADABLES;
        PATH_TO_DOWNLOADABLES_XML=this.PATH_TO_DOWNLOADABLES;
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
    public void generateBankStatement(FORMAT_TYPE reportFormat, LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException, JRException {
        log.debug("################# Pdf Creation Service started ###################################");
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
                .startDate(startDate)
                .endDate(endDate)
                .build();

        validationService.transactionsUpdateValidator(loadAccount,null,bankStatement,GEN_BANK_STATEMENT);
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
                new ArrayList<>(transactionsListBetweenDate.stream().map(MapperHelper::mapToTransactionsInvoicableObject).toList());

        Comparator<TransactionsInvoicableObject> sortByTimeStampInAscendingOrderOfLatestTransaction=(o1,o2)->
                (convertTimeStampToLocalDateTime(o1.getTransactionTimeStamp())
                        .isBefore(convertTimeStampToLocalDateTime(o2.getTransactionTimeStamp())))? -1:
                        (convertTimeStampToLocalDateTime(o1.getTransactionTimeStamp())
                                .isAfter(convertTimeStampToLocalDateTime(o2.getTransactionTimeStamp())))? 1:0;

        listOfTransactionsBetweenDate.sort(sortByTimeStampInAscendingOrderOfLatestTransaction);

        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(listOfTransactionsBetweenDate);
        JasperReport report= JasperCompileManager.compileReport(PATH_TO_JASPER_XML);
        JasperPrint print= JasperFillManager.fillReport(report,params,dataSource);
        final String accountUID=bankStatement.getAccountNumber();

        switch (reportFormat){
            case PDF -> {
                PATH_TO_DOWNLOADABLES_PDF+=String.format("%s.pdf",accountUID);
                JasperExportManager.exportReportToPdfFile(print,PATH_TO_DOWNLOADABLES_PDF);
                reset();
            }
            case HTML -> {
                PATH_TO_DOWNLOADABLES_HTML+=String.format("%s.html",accountUID);
                JasperExportManager.exportReportToHtmlFile(print,PATH_TO_DOWNLOADABLES_HTML);
                reset();
            }
            case XML -> {
                PATH_TO_DOWNLOADABLES_XML+=String.format("%s.xml",accountUID);
                JasperExportManager.exportReportToXmlFile(print,PATH_TO_DOWNLOADABLES_XML,false);
                reset();
            }
        }
        log.debug("################# Pdf Creation Service ended ###################################");
    }
}
