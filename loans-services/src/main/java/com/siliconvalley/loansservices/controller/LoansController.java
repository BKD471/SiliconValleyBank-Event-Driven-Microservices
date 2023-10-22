package com.siliconvalley.loansservices.controller;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import com.siliconvalley.loansservices.service.ILoansService;
import com.siliconvalley.loansservices.exception.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoansController {
    private final ILoansService loanService;
    LoansController(@Qualifier("loanServicePrimary") ILoansService loanService){
        this.loanService=loanService;
    }
    @PostMapping
    public ResponseEntity<OutPutDto> borrowLoan(@RequestBody LoansDto loansDto) throws TenureException, ValidationException, PaymentException, LoansException, InstallmentsException {
        OutPutDto processedLoansDto=loanService.loansExecutor(loansDto);
        return new ResponseEntity<>(processedLoansDto, HttpStatus.CREATED);
    }

    @PutMapping("/v1/pay/emi")
    public ResponseEntity<OutPutDto> payEmi(@RequestBody LoansDto loansDto) throws PaymentException, InstallmentsException, LoansException, TenureException, ValidationException {
      OutPutDto paidEmi=loanService.loansExecutor(loansDto);
      return new ResponseEntity<>(paidEmi,HttpStatus.ACCEPTED);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<OutPutDto> getAllLoansByCustomerId(@RequestBody final LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException {
       OutPutDto allLoans=loanService.loansExecutor(loansDto);
       return new ResponseEntity<>(allLoans,HttpStatus.OK);
    }

    @GetMapping("/v1/{id}/{num}")
    public  ResponseEntity<OutPutDto> getInfoAboutLoanByCustomerIdAndLoanNumber
            (@RequestBody LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException, TenureException {
        OutPutDto loan=loanService.loansExecutor(loansDto);
        return new ResponseEntity<>(loan,HttpStatus.OK);
    }
}
