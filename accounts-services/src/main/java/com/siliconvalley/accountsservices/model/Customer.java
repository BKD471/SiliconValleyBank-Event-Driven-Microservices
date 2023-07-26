package com.siliconvalley.accountsservices.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Audit implements UserDetails {
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
    private List<Accounts> accounts=new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Role> roles=new HashSet<>();

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role->new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
    }

    /**
     * @return
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * @return
     */
    @Override
    public String getPassword(){
        return this.password;
    }
    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
