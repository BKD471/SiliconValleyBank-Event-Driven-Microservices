package com.siliconvalley.accountsservices.service;

import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public interface IPdfService {
    ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate,String accountNumber) throws FileNotFoundException;
    String generateBankStatement(String accountNumber,LocalDate startDate,LocalDate endDate) throws JRException;
}
