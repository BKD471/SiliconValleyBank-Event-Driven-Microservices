package com.example.accountsservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BaseDto extends  AbstractParentDto{
    protected int age;
    protected String email;
    protected String phoneNumber;
    protected String adharNumber;
    protected String panNumber;
    protected String voterId;
    protected String drivingLicense;
    protected String passportNumber;
}
