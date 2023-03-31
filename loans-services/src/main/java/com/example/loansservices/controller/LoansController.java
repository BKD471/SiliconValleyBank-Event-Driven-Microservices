package com.example.loansservices.controller;

import com.example.loansservices.dto.LoansDto;
import com.example.loansservices.dto.PaymentDto;
import com.example.loansservices.exception.InstallmentsException;
import com.example.loansservices.exception.PaymentException;
import com.example.loansservices.exception.TenureException;
import com.example.loansservices.service.impl.LoanServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoansController {
    private  LoanServiceImpl loanService;
    LoansController(LoanServiceImpl loanService){
        this.loanService=loanService;
    }
    @PostMapping
    public ResponseEntity<LoansDto> borrowLoan(@RequestBody LoansDto loansDto) throws TenureException {
        LoansDto processedLoansDto=loanService.borrowLoan(loansDto);
        return new ResponseEntity<>(processedLoansDto, HttpStatus.CREATED);
    }

    @PutMapping("/pay/emi")
    public ResponseEntity<PaymentDto> payEmi(@RequestBody PaymentDto paymentDto) throws PaymentException, InstallmentsException {
      PaymentDto paidEmi=loanService.payInstallments(paymentDto);
      return new ResponseEntity<>(paidEmi,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<LoansDto>> getAllLoansByCustomerId
            (@PathVariable(name="id") Long customerId){
       List<LoansDto> allLoans=loanService.getAllLoansForCustomerById(customerId);
       return new ResponseEntity(allLoans,HttpStatus.OK);
    }

    @GetMapping("/{id}/{num}")
    public  ResponseEntity<LoansDto> getInfoAboutLoanByCustomerIdAndLoanNumber
            (@PathVariable(name ="id") Long customerId, @PathVariable(name="num") Long loanNumber){
        LoansDto loan=loanService.getInfoAboutLoanByCustomerIdAndLoanNumber(customerId, loanNumber);
        return new ResponseEntity(loan,HttpStatus.OK);
    }
}
