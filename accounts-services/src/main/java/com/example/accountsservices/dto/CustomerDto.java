package com.example.accountsservices.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long customerId;
    private LocalDate DateOfBirth;
    private String customerName;
    private int age;
    private String email;
    private String password;
    private String phoneNumber;
    private String adharNumber;
    private String panNumber;
    private String voterId;
    private String drivingLicense;
    private String passportNumber;
    private String address;
    private String imageName;
    private MultipartFile image;
    private List<AccountsDto> accounts;
 }
