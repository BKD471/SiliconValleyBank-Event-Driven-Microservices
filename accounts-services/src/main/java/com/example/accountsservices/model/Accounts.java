package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Accounts extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "account_num")
    private Long accountNumber;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(nullable = false, name = "accnt_type")
    private String accountType;
    @Column(nullable = false, name = "branch_addr")
    private String branchAddress;

    @Column(name = "phone_num", nullable = false)
    private String phoneNumber;

    @Column(name = "adhar_num", nullable = false)
    private String adharNumber;

    @Column(name = "pan_num", nullable = false)
    private String panNumber;

    @Column(name = "voter_id")
    private String voterId;

    @Column(name = "driving_license")
    private String drivingLicense;

    @Column(name = "passport")
    private String passportNumber;

    @OneToOne
    private  Beneficiary beneficiary;
}
