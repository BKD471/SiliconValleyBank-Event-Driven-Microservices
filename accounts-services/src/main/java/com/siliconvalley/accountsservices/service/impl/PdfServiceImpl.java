package com.siliconvalley.accountsservices.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.siliconvalley.accountsservices.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Slf4j
@Service
public class PdfServiceImpl implements IPdfService {
    @Override
    public ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate){
        log.info("################# Pdf Creation Service started ###################################");

        String title="Testing testing";
        String content="Hey sexy lady, i like yr flow";
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();
        PdfWriter.getInstance(document,out);


        HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);
        document.open();

        Font titleFont= FontFactory.getFont(FontFactory.COURIER_BOLD,24);
        Paragraph titlePara=new Paragraph(title,titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,18);
        Paragraph paragraph=new Paragraph(content,paraFont);
        //paragraph.add(new Chunk("..................."));
        document.add(paragraph);

        document.close();
        log.info("################# Pdf Creation Service ended #######################################");
        return new ByteArrayInputStream(out.toByteArray());
    }
}
