package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.controller.ITransactionsController;
import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.service.IPdfService;
import com.siliconvalley.accountsservices.service.ITransactionsService;
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
                               @Qualifier("OpenPdfImplementation") IPdfService pdfService) {
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

    @Override
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
}
