package com.siliconvalley.accountsservices.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public interface IPdfService {
    ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate,String accountNumber) throws FileNotFoundException;
}
