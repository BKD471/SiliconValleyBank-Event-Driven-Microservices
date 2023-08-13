package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.BankStatement;
import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.repository.ITransactionsRepository;
import com.siliconvalley.accountsservices.service.AbstractPdfService;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.time.LocalDate;

@Slf4j
@Service("jasperPdfService")
public class PdfServiceJasperImpl extends AbstractPdfService {
    private final ITransactionsRepository transactionsRepository;
    private final IAccountsRepository accountsRepository;

    protected PdfServiceJasperImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository,
                                   ITransactionsRepository transactionsRepository) {
        super(accountsRepository, customerRepository);
        this.accountsRepository=accountsRepository;
        this.transactionsRepository=transactionsRepository;
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
        Accounts loadAccount=fetchAccountByAccountNumber(accountNumber);
        Set<Transactions> setOfTransactions=prepareTransactionsSetBetweenDate(startDate,endDate,accountNumber);
        BankStatement bankStatement=BankStatement.builder()
                .accountName(loadAccount.getCustomer().getName())
                .accountNumber(loadAccount.getAccountNumber())
                .accountType(loadAccount.getAccountType().toString())
                .branch(loadAccount.getHomeBranch().toString())
                .RateOfInterest(loadAccount.getRateOfInterest())
                .balance(loadAccount.getBalance())
                .listOfTransaction(loadAccount.getListOfTransactions()).build();


        File file= ResourceUtils.getFile("classpath:BankStatement.jrxml");
        JasperReport jasperReport= JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(setOfTransactions);
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("Created By","Silicon Valley Corp");
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if(reportFormat.toString().equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,"C:\\Users\\Bhaskar\\Desktop\\Spring\\Banks Services\\bank Stmt Report"+"\bankStatement.html");
        }
        if(reportFormat.toString().equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,"C:\\Users\\Bhaskar\\Desktop\\Spring\\Banks Services\\bank Stmt Report"+"\bankStatement.pdf");

        }

        return "Report Generated";
    }

}
