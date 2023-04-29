package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
