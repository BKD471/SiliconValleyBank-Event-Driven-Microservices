package com.siliconvalley.loansservices.controller;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import com.siliconvalley.loansservices.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/loans")
public interface ILoansController {
    @PostMapping("/v1/borrow")
    ResponseEntity<OutPutDto> borrowLoan(@RequestBody LoansDto loansDto) throws TenureException, ValidationException, PaymentException, LoansException, InstallmentsException;
    @PutMapping("/v1/pay/emi")
    ResponseEntity<OutPutDto> payEmi(@RequestBody LoansDto loansDto) throws PaymentException, InstallmentsException, LoansException, TenureException, ValidationException;

    @GetMapping("/v1/getAll")
    ResponseEntity<OutPutDto> getAllLoansByCustomerId(@RequestBody final LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException;

    @GetMapping("/v1/getInfo")
    ResponseEntity<OutPutDto> getInfoAboutLoanByCustomerIdAndLoanNumber
            (@RequestBody final LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException;
}
