package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.AbstractParentDto;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOutPutDto extends AbstractParentDto {
    private Long customerId;
    private String customerName;
    private String email;
    private LocalDate DateOfBirth;
    private int age;
    private String phoneNumber;
    private String adharNumber;
    private String panNumber;
    private String voterId;
    private String drivingLicense;
    private String passportNumber;
    private String address;
    private String imageName;
}
