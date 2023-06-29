package com.example.accountsservices.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary extends Audit{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ben_id")
    private long beneficiaryId;

    @Column(name = "ben_name",nullable = false)
    private String beneficiaryName;


    public enum BanksSupported{
        SBI,AXIS,HDFC,ICICI,CANARA,PNB,ORIENTAL,BOI,YES,BANDHAN,BOB
    }

    @Column(name="ben_bank",nullable = false)
    @Enumerated(EnumType.STRING)
    private BanksSupported benBank;

    private String bankCode;

    @Column(name = "ben_acnt_num",nullable = false)
    private long beneficiaryAccountNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RELATION relation;

    public enum RELATION{
        FATHER,MOTHER,SPOUSE,SON,DAUGHTER
    }

    @Email
    @Column(name="ben_email",nullable = false)
    private String beneficiaryEmail;

    @Column(name="dob",nullable = false)
    private LocalDate BenDate_Of_Birth;

    private int benAge;

    @Column(name = "adhar_num",nullable = false)
    private String benAdharNumber;

    @Column(name="phone_num",nullable = false)
    private String benPhoneNumber;

    @Column(name = "pan_num",nullable = false)
    private String benPanNumber;

    @Column(name = "voter_id")
    private String benVoterId;

    @Column(name = "driving_license")
    private String benDrivingLicense;

    @Column(name = "passport")
    private String benPassportNumber;

    @Column(name = "address",length = 1000)
    private String address;

    @Column(name = "img_name",length = 256)
    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id",nullable = false)
    private Accounts accounts;
}
