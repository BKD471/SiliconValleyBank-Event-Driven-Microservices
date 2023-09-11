package com.siliconvalley.loansservices.service;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import com.siliconvalley.loansservices.exception.*;

public interface ILoansService {
    OutPutDto transactionsExecutor(final LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException;
}
