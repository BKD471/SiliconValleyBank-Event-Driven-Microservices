package com.example.accountsservices.dto.outputDtos;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOutPutDto {
    private Long customerId;
    private String customerName;
    protected String email;
    protected LocalDate DateOfBirth;
    protected int age;
    protected String phoneNumber;
    protected String adharNumber;
    protected String panNumber;
    protected String voterId;
    protected String drivingLicense;
    protected String passportNumber;
    private String address;
    private String imageName;
}
