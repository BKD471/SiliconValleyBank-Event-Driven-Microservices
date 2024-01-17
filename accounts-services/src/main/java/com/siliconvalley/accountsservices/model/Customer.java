package com.siliconvalley.accountsservices.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Email;


import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//public class Customer extends Audit implements UserDetails,Comparable<Customer>
public class Customer extends Audit implements Comparable<Customer>{
    @Id
    private String customerId;

    @Column(nullable = false,name="cust_name")
    private String name;

    @Column(name="dob",nullable = false)
    private LocalDate DateOfBirth;

    private int age;

    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,length = 1024)
    private String password;

    @Column(nullable = false,unique = true,name="mobile_num")
    private String phoneNumber;

    @Column(name = "adhar_num",unique = true, nullable = false)
    private String adharNumber;

    @Column(name = "pan_num",unique = true, nullable = false)
    private String panNumber;

    @Column(name = "voter_id",unique = true)
    private String voterId;

    @Column(name = "driving_license",unique = true)
    private String drivingLicense;

    @Column(name = "passport",unique = true)
    private String passportNumber;

    @Column(name = "img_name",length = 256)
    private String imageName;

    @Column(name = "address",length = 1000)
    private String address;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Accounts> accounts=new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Role> roles=new HashSet<>();

    /**
     * @param cust
     * @return
     */
    @Override
    public int compareTo(Customer cust) {
        int numOfAccounts=cust.getAccounts().size();
        if(numOfAccounts==accounts.size()) return  0;
        else if(numOfAccounts>accounts.size()) return -1;
        else return 1;
    }
}
