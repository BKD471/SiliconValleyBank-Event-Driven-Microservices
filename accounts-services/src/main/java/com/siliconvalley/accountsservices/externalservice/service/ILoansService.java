package com.siliconvalley.accountsservices.externalservice.service;

import com.siliconvalley.accountsservices.externalservice.exception.ServiceDownException;
import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "loansFallBack",fallbackMethod = "fallback")
@RateLimiter(name = "loansLimiter")
@FeignClient(name = "LOANSSERVICE/api/loans")
public interface ILoansService {
    @PostMapping("/v1/borrow")
    ResponseEntity<OutPutDto> borrowLoan(@RequestBody LoansDto loansDto);
    @PutMapping("/v1/pay/emi")
    ResponseEntity<OutPutDto> payEmi(@RequestBody LoansDto loansDto);
    @GetMapping("/v1/getAll")
    ResponseEntity<OutPutDto> getAllLoansByCustomerId(@RequestBody final LoansDto loansDto);
    @GetMapping("/v1/getInfo")
    ResponseEntity<OutPutDto> getInfoAboutLoanByCustomerIdAndLoanNumber
            (@RequestBody final LoansDto loansDto);

    default ResponseEntity<OutPutDto> fallback(LoansDto loansDto, CallNotPermittedException e1) {
        throw new ServiceDownException("Loans Service is down ", "SERVER_NOT_AVAILABLE",404);
    }

}
