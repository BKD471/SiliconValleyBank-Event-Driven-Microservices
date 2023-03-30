package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="benficiary_id")
    private Long beneficiary_id;

    @Column(name = "name",nullable = false)
    private String beneficiary_name;

    @Column(nullable = false)
    private String relation;

    @Column(name="dob",nullable = false)
    private Date Date_Of_Birth;
    private int age;
    @Column(name = "adhar_num",nullable = false)
    private String adharNumber;

    @Column(name = "pan_num",nullable = false)
    private String panNumber;

    private String voterId;

    private String passPrt;
}
