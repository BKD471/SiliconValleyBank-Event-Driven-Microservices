package com.example.bankdata.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false,name="cust_name")
    private String name;

    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true,name="mobile_num")
    private String mobileNumber;

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
}
