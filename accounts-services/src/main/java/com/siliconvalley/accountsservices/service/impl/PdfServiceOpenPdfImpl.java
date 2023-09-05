package com.siliconvalley.accountsservices.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.BankStatement;
import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.AbstractPdfService;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import java.util.Set;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service("OpenPdfImplementation")
public class PdfServiceOpenPdfImpl extends AbstractPdfService{
    protected PdfServiceOpenPdfImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }


    private PdfPTable addTable() throws FileNotFoundException {
        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Document document = new Document();
        PdfPTable table = null;
        try {
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream("tables.pdf"));
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            // step 3
            document.open();

            // step 4
            float[] columnDefinitionSize = {33.33F, 33.33F, 33.33F};

            float pos = height / 2;
            PdfPCell cell = null;

            table = new PdfPTable(columnDefinitionSize);
            table.getDefaultCell().setBorder(0);
            table.setHorizontalAlignment(0);
            table.setTotalWidth(width - 72);
            table.setLockedWidth(true);

            cell = new PdfPCell(new Phrase("Table added with document.add()"));
            cell.setColspan(columnDefinitionSize.length);
            table.addCell(cell);
            table.addCell(new Phrase("Louis Pasteur", font8));
            table.addCell(new Phrase("Albert Einstein", font8));
            table.addCell(new Phrase("Isaac Newton", font8));
            table.addCell(new Phrase("8, Rabic street", font8));
            table.addCell(new Phrase("2 Photons Avenue", font8));
            table.addCell(new Phrase("32 Gravitation Court", font8));
            table.addCell(new Phrase("39100 Dole France", font8));
            table.addCell(new Phrase("12345 Ulm Germany", font8));
            table.addCell(new Phrase("45789 Cambridge  England", font8));
        } catch (DocumentException | IOException e) {
            log.error(String.valueOf(e));
        }
        return table;
    }

    @Override
    public ByteArrayInputStream generateBankStatement(LocalDate startDate, LocalDate endDate, String accountNumber) throws
            FileNotFoundException {
        log.info("################# Pdf Creation Service started ###################################");

        String title = "Silicon Valley Corporation Pvt Ltd Account Statement";
        Set<Transactions> transactionsListBetweenDate = prepareTransactionsSetBetweenDate(startDate, endDate, accountNumber);
        Accounts loadAccount = fetchAccountByAccountNumber(accountNumber);

        BankStatement bankStatement = BankStatement.builder()
                .accountName(loadAccount.getCustomer().getName())
                .accountNumber(loadAccount.getAccountNumber())
                .accountType(loadAccount.getAccountType().toString())
                .branch(loadAccount.getHomeBranch().toString())
                .rateOfInterest(loadAccount.getRateOfInterest())
                .balance(loadAccount.getBalance())
                .listOfTransaction(transactionsListBetweenDate)
                .build();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);


        HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Paragraph titlePara = new Paragraph(title, titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        Font paraFont = FontFactory.getFont(FontFactory.HELVETICA, 18);
        Paragraph paragraph = new Paragraph(bankStatement.toString(), paraFont);
        //paragraph.add(new Chunk("..................."));

        PdfPTable table=addTable();

        document.add(paragraph);
        document.add(table);

        document.close();
        log.info("################# Pdf Creation Service ended #######################################");
        return new ByteArrayInputStream(out.toByteArray());
    }


}
