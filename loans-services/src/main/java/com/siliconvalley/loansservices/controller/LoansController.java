package com.siliconvalley.loansservices.controller;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import com.siliconvalley.loansservices.service.ILoansService;
import com.siliconvalley.loansservices.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
public class LoansController {
    private final ILoansService loanService;
    LoansController(ILoansService loanService){this.loanService=loanService;}
    @PostMapping
    public ResponseEntity<OutPutDto> borrowLoan(@RequestBody LoansDto loansDto) throws TenureException, ValidationException, PaymentException, LoansException, InstallmentsException {
        OutPutDto processedLoansDto=loanService.transactionsExecutor(loansDto);
        return new ResponseEntity<>(processedLoansDto, HttpStatus.CREATED);
    }

    @PutMapping("/pay/emi")
    public ResponseEntity<OutPutDto> payEmi(@RequestBody LoansDto loansDto) throws PaymentException, InstallmentsException, LoansException, TenureException, ValidationException {
      OutPutDto paidEmi=loanService.transactionsExecutor(loansDto);
      return new ResponseEntity<>(paidEmi,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutPutDto> getAllLoansByCustomerId(@RequestBody final LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException {
       OutPutDto allLoans=loanService.transactionsExecutor(loansDto);
       return new ResponseEntity<>(allLoans,HttpStatus.OK);
    }

    @GetMapping("/{id}/{num}")
    public  ResponseEntity<OutPutDto> getInfoAboutLoanByCustomerIdAndLoanNumber
            (@RequestBody LoansDto loansDto) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        OutPutDto loan=loanService.transactionsExecutor(loansDto);
        return new ResponseEntity<>(loan,HttpStatus.OK);
    }
}
