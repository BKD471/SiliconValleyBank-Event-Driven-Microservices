package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputDto extends BaseDto{
    private String name;
    private String dateOfBirthInYMD;
    private Accounts.AccountType accountType;
    private Accounts.Branch homeBranch;
}
