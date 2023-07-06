package com.example.accountsservices.dto.baseDtos;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class CustomerDto {
    private final String customerId;
    private final LocalDate DateOfBirth;
    private final String customerName;
    private final int age;
    private final String email;
    //private String password;
    private final String phoneNumber;
    private final String adharNumber;
    private final String panNumber;
    private final String voterId;
    private final String drivingLicense;
    private final String passportNumber;
    private final String address;
    private final String imageName;
    private final MultipartFile customerImage;
    private final List<AccountsDto> accounts;
 }
