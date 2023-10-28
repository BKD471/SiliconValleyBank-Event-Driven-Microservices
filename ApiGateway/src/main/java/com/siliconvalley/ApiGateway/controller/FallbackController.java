package com.siliconvalley.ApiGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/accountsServiceFallBack")
    public String accountsServiceFallBack(){
        return "Accounts Service is down";
    }
    @GetMapping("/loansServiceFallBack")
    public String loansServiceFallBack(){
        return "Loans Service is down";
    }
    @GetMapping("/paymentsServiceFallBack")
    public String paymentsServiceFallBack(){
        return "Payments Service is down";
    }

    @GetMapping("/cardsServiceFallBack")
    public String cardsServiceFallBack(){
        return "Cards Service is down";
    }

    @GetMapping("/emailServiceFallBack")
    public String emailServiceFallBack(){
        return "Email Service is down";
    }
}
