package com.siliconvalley.accountsservices.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.BankStatement;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Slf4j
@Service("OpenPdfImplementation")
public class PdfServiceOpenPdfImpl extends AbstractService implements IPdfService {
    protected PdfServiceOpenPdfImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    @Override
    public ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate,String accountNumber){
        log.info("################# Pdf Creation Service started ###################################");

        String title="Silicon Valley Corporation Pvt Ltd Account Statement";

        Accounts loadAccount=fetchAccountByAccountNumber(accountNumber);
        BankStatement bankStatement=BankStatement.builder()
                .accountName(loadAccount.getCustomer().getName())
                .accountNumber(loadAccount.getAccountNumber())
                .accountType(loadAccount.getAccountType())
                .branch(loadAccount.getHomeBranch())
                .RateOfInterest(loadAccount.getRateOfInterest())
                .balance(loadAccount.getBalance())
                .build();

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();
        PdfWriter.getInstance(document,out);


        HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);
        document.open();

        Font titleFont= FontFactory.getFont(FontFactory.HELVETICA_BOLD,24);
        Paragraph titlePara=new Paragraph(title,titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,18);
        Paragraph paragraph=new Paragraph(bankStatement.toString(),paraFont);
        //paragraph.add(new Chunk("..................."));
        document.add(paragraph);

        document.close();
        log.info("################# Pdf Creation Service ended #######################################");
        return new ByteArrayInputStream(out.toByteArray());
    }
}
