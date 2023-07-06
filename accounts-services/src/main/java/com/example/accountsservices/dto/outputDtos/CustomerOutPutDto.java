package com.example.accountsservices.dto.outputDtos;

import lombok.*;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class CustomerOutPutDto {
    private final String customerId;
    private final String customerName;
    private final String email;
    private final LocalDate DateOfBirth;
    private final int age;
    private final String phoneNumber;
    private final String adharNumber;
    private final String panNumber;
    private final String voterId;
    private final String drivingLicense;
    private final String passportNumber;
    private final String address;
    private final String imageName;
}
