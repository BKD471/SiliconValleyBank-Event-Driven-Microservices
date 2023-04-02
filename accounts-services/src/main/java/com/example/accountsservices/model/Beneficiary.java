package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary extends Audit{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ben_id")
    private Long beneficiaryId;

    @Column(name = "ben_name",nullable = false)
    private String beneficiaryName;

    @Column(name = "ben_acnt_num")
    private Long beneficiaryAccountNumber;
    @Column(nullable = false)
    private RELATION relation;

    public enum RELATION{
        FATHER,MOTHER,SPOUSE,SON,DAUGHTER
    }

    @Column(name="dob",nullable = false)
    private LocalDate Date_Of_Birth;

    private int age;

    @Column(name = "adhar_num",nullable = false)
    private String adharNumber;

    @Column(name = "pan_num",nullable = false)
    private String panNumber;

    private String voterId;

    private String passPort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id",nullable = false)
    private Accounts accounts;
}
