package com.example.basedomain.model;



import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerDto {
    private Long customerId;
    private String name;
    private String email;
    private String mobileNumber;
}
