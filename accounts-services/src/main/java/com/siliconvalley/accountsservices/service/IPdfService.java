package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public interface IPdfService {
    ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate,String accountNumber) throws FileNotFoundException;
    void generateBankStatement(AllConstantHelpers.FORMAT_TYPE reportFormat, LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException, JRException;
}
