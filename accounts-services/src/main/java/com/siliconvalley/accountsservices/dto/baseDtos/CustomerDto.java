package com.siliconvalley.accountsservices.dto.baseDtos;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String customerId;
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
    private MultipartFile customerImage;
    private List<AccountsDto> accounts;
    private Set<RoleDto> roles=new HashSet<>();
}
