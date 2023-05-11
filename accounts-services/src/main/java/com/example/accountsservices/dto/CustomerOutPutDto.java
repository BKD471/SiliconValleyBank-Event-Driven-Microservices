package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOutPutDto {
    protected int age;
    protected String email;
    protected String phoneNumber;
    protected String adharNumber;
    protected String panNumber;
    protected String voterId;
    protected String drivingLicense;
    protected String passportNumber;
    private Long customerId;
    protected LocalDate DateOfBirth;
    private String customerName;
}
