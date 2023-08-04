package com.siliconvalley.accountsservices.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

public interface IPdfService {
    ByteArrayInputStream generateBankStatement(LocalDate startDate,LocalDate endDate,String accountNumber);
}
