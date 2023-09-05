package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public interface IPdfService {
    ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate,String accountNumber) throws FileNotFoundException;
    String generateBankStatement(BankStatementRequestDto.FORMAT_TYPE reportFormat, LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException, JRException;
}
