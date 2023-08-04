package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@Slf4j
@Service("jasperPdfService")
public class PdfServiceJasperImpl extends AbstractService implements IPdfService {
    protected PdfServiceJasperImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public ByteArrayInputStream generateBankStatement(LocalDate startDate, LocalDate endDate,String accountNumber) {
        return null;
    }
}
