package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class BeneficiaryDto implements  Dto{
    private Long beneficiaryId;
    private String beneficiaryName;
    private Long beneficiaryAccountNumber;
    private Beneficiary.RELATION relation;
    private Date Date_Of_Birth;
    private int age;
    private String adharNumber;
    private String panNumber;
    private String voterId;
    private String passPort;
    private Accounts accounts;
}
