package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.IPdfService;
import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

//adapter design pattern
public abstract class AbstractPdfService extends AbstractService implements IPdfService {
    protected AbstractPdfService(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    //dummy implementations
    public ByteArrayInputStream generateBankStatement(LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException{
        return null;
    }
    public void generateBankStatement(AllConstantHelpers.FORMAT_TYPE reportFormat, LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException, JRException{
    }
}
