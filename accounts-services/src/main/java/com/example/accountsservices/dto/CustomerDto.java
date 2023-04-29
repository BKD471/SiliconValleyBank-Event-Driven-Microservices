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
public class CustomerDto extends BaseDto{
    private Long customerId;
    protected LocalDate DateOfBirth;
    private String customerName;
    private List<AccountsDto> accounts;
 }
