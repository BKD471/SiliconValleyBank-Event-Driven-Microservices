package com.siliconvalley.loansservices.controller;

import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.PaymentDto;
import com.siliconvalley.loansservices.service.ILoansService;
import com.siliconvalley.loansservices.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoansController {
    private final ILoansService loanService;
    LoansController(ILoansService loanService){this.loanService=loanService;}
    @PostMapping
    public ResponseEntity<LoansDto> borrowLoan(@RequestBody LoansDto loansDto) throws TenureException, ValidationException, PaymentException, LoansException, InstallmentsException {
        LoansDto processedLoansDto=loanService.borrowLoan(loansDto);
        return new ResponseEntity<>(processedLoansDto, HttpStatus.CREATED);
    }

    @PutMapping("/pay/emi")
    public ResponseEntity<PaymentDto> payEmi(@RequestBody PaymentDto paymentDto) throws PaymentException, InstallmentsException, LoansException, TenureException, ValidationException {
      PaymentDto paidEmi=loanService.payInstallments(paymentDto);
      return new ResponseEntity<>(paidEmi,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<LoansDto>> getAllLoansByCustomerId
            (@PathVariable(name="id") String customerId) throws LoansException, ValidationException, PaymentException, InstallmentsException {
       List<LoansDto> allLoans=loanService.getAllLoansForACustomer(customerId);
       return new ResponseEntity<>(allLoans,HttpStatus.OK);
    }

    @GetMapping("/{id}/{num}")
    public  ResponseEntity<LoansDto> getInfoAboutLoanByCustomerIdAndLoanNumber
            (@PathVariable(name ="id") String customerId, @PathVariable(name="num") String loanNumber) throws LoansException, ValidationException, PaymentException, InstallmentsException {
        LoansDto loan=loanService.getInfoAboutAParticularLoan(customerId, loanNumber);
        return new ResponseEntity<>(loan,HttpStatus.OK);
    }
}
