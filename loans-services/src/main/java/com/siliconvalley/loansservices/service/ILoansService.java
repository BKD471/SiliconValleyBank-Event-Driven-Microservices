package com.siliconvalley.loansservices.service;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import com.siliconvalley.loansservices.exception.*;

public interface ILoansService {
    OutPutDto loansExecutor(final LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException;
}
