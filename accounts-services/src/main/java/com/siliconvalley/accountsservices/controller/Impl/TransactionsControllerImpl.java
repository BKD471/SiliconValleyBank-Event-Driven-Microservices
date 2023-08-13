package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.controller.ITransactionsController;
import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.service.AbstractPdfService;
import com.siliconvalley.accountsservices.service.IPdfService;
import com.siliconvalley.accountsservices.service.ITransactionsService;
import fansi.Str;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

import static com.siliconvalley.accountsservices.helpers.MapperHelper.dateParserInYYYYMMDD;

@RestController
public class TransactionsControllerImpl implements ITransactionsController {
    private final ITransactionsService transactionsService;
    private final IPdfService pdfService;

    TransactionsControllerImpl(@Qualifier("transactionsServicePrimary") ITransactionsService transactionsService,
                               @Qualifier("jasperPdfService") IPdfService pdfService) {
        this.transactionsService = transactionsService;
        this.pdfService=pdfService;
    }

    /**
     * @param transactionsDto
     * @return
     * @throws TransactionException
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> executeTransactions(final TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        final OutputDto responseDto = transactionsService.transactionsExecutor(transactionsDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<OutputDto> getPastSixMonthsTransaction(final String accountNumber) throws AccountsException {
        final OutputDto responseDto = transactionsService.getPastSixMonthsTransactionsForAnAccount(accountNumber);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    public ResponseEntity<InputStreamResource> generateBankStatement(final BankStatementRequestDto bankStatementRequestDto) throws FileNotFoundException {
        String accountNumber=bankStatementRequestDto.getAccountNumber();
        LocalDate startDate=dateParserInYYYYMMDD(bankStatementRequestDto.getStartDate());
        LocalDate endDate=dateParserInYYYYMMDD(bankStatementRequestDto.getEndDate());

        ByteArrayInputStream pdf=pdfService.generateBankStatement(startDate,endDate,accountNumber);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=bkd.pdf");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    /**
     * @param bankStatementRequestDto
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public ResponseEntity<String> generateBankStatementAnyFormat(BankStatementRequestDto bankStatementRequestDto) throws FileNotFoundException, JRException {
        String accountNumber=bankStatementRequestDto.getAccountNumber();
        LocalDate startDate=dateParserInYYYYMMDD(bankStatementRequestDto.getStartDate());
        LocalDate endDate=dateParserInYYYYMMDD(bankStatementRequestDto.getEndDate());
        BankStatementRequestDto.FORMAT_TYPE downloadableFORMAT=bankStatementRequestDto.getDownloadFormat();
        String res=pdfService.generateBankStatement(downloadableFORMAT,startDate,endDate,accountNumber);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


}
