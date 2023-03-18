package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


@Getter
@Setter
@Entity
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true,name="accnt_num")
    private Long accountNumber;

    @Column(name = "cust_id")
    private Long customerId;

    @Column(nullable = false,name="accnt_type")
    private  String accountType;
    @Column(nullable = false,name="branch_addr")
    private String branchAddress;
    @CreatedDate
    private LocalDate createdDate;
}
