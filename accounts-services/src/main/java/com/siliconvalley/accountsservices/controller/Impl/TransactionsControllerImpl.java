package com.siliconvalley.accountsservices.controller.Impl;

import com.siliconvalley.accountsservices.controller.ITransactionsController;
import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.service.IPdfService;
import com.siliconvalley.accountsservices.service.ITransactionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Properties;

import static com.siliconvalley.accountsservices.helpers.MapperHelper.dateParserInYYYYMMDD;

@Slf4j
@RestController
@Tag(name = "TransactionsController",description = "APi for transactions")
public class TransactionsControllerImpl implements ITransactionsController {
    private final ITransactionsService transactionsService;
    private final IPdfService pdfService;
    private final String fileBasePath;
    private final String fileName;


    TransactionsControllerImpl(@Qualifier("transactionsServicePrimary") ITransactionsService transactionsService,
                               @Qualifier("jasperPdfService") IPdfService pdfService,@Value("${path.controller.transactions}") String path_transaction_controller_properties) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path_transaction_controller_properties));
        }catch (IOException e){
            log.error("Error while reading {}'s properties file {}",this.getClass().getSimpleName(),e.getMessage());
        }
        this.transactionsService = transactionsService;
        this.pdfService=pdfService;
        this.fileBasePath= properties.getProperty("fileBasePath");
        this.fileName= properties.getProperty("fileName");
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




    @Override
    public ResponseEntity<InputStreamResource> generateBankStatementPdf(final BankStatementRequestDto bankStatementRequestDto) throws FileNotFoundException {

        String accountNumber=bankStatementRequestDto.accountNumber();
        LocalDate startDate=dateParserInYYYYMMDD(bankStatementRequestDto.startDate());
        LocalDate endDate=dateParserInYYYYMMDD(bankStatementRequestDto.endDate());

        ByteArrayInputStream pdf=pdfService.generateBankStatement(startDate,endDate,accountNumber);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=bkd.pdf");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }


    private ResponseEntity<Resource> generateStatementForAnyFormat(final String accountNumber, final AllConstantHelpers.FORMAT_TYPE formatType) throws IOException {
        final String methodName="generateStatementForAnyFormat(accountNumber,formatType) in TransactionsController";
        String fieldIdWithExtension = null;
        switch (formatType) {
            case PDF -> fieldIdWithExtension = String.format("%s.pdf", accountNumber);
            case HTML -> fieldIdWithExtension = String.format("%s.html", accountNumber);
            case XML -> fieldIdWithExtension = String.format("%s.xml", accountNumber);
        }
        Path path = Paths.get(fileBasePath + fileName + fieldIdWithExtension);
        Resource resource = new UrlResource(path.toUri());
        ByteArrayInputStream stream = new ByteArrayInputStream(resource.getContentAsByteArray());

        switch (formatType) {
            case PDF -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + fieldIdWithExtension + "\"")
                        .body(new InputStreamResource(stream));
            }
            case HTML -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + fieldIdWithExtension + "\"")
                        .body(new InputStreamResource(stream));
            }
            case XML -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_XML)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + fieldIdWithExtension + "\"")
                        .body(new InputStreamResource(stream));
            }
            default ->
                    throw new BadApiRequestException(BadApiRequestException.class, "Your request format is not supported", methodName);

        }
    }
    /**
     * @param transactionsDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<Resource> getPastSixMonthsTransaction(final TransactionsDto transactionsDto) throws AccountsException, JRException, IOException {
        final String accountNumber=transactionsDto.accountNumber();
        final AllConstantHelpers.FORMAT_TYPE formatType=transactionsDto.downloadFormat();
        transactionsService.getPastSixMonthsTransactionsForAnAccount(accountNumber, formatType);
        return generateStatementForAnyFormat(accountNumber,formatType);
    }

    /**
     * @param bankStatementRequestDto
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public ResponseEntity<Resource> generateBankStatementAnyFormat(BankStatementRequestDto bankStatementRequestDto) throws IOException, JRException {
        String accountNumber=bankStatementRequestDto.accountNumber();
        LocalDate startDate=dateParserInYYYYMMDD(bankStatementRequestDto.startDate());
        LocalDate endDate=dateParserInYYYYMMDD(bankStatementRequestDto.endDate());
        AllConstantHelpers.FORMAT_TYPE downloadableFORMAT=bankStatementRequestDto.downloadFormat();
        pdfService.generateBankStatement(downloadableFORMAT,startDate,endDate,accountNumber);
        return generateStatementForAnyFormat(accountNumber,downloadableFORMAT);
    }
}
