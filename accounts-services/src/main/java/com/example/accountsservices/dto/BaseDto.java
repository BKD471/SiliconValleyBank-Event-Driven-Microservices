package com.example.accountsservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {
    protected LocalDate DateOfBirth;
    protected int age;
    protected String phoneNumber;
    protected String adharNumber;
    protected String panNumber;
    protected String voterId;
    protected String drivingLicense;
    protected String passportNumber;
}
