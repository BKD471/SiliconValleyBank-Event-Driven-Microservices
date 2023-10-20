package com.siliconvalley.accountsservices.dto.tokenDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;

public record JwtResponse(String jwtToken,CustomerDto customer){
  public static final class Builder{
    private String jwtToken;
    private CustomerDto customer;

    public Builder(){}
    public  Builder jwtToken(String jwtToken){
      this.jwtToken=jwtToken;
      return this;
    }

    public Builder customer(CustomerDto customer){
      this.customer=customer;
      return this;
    }

    public JwtResponse build(){
      return new JwtResponse(jwtToken,customer);
    }
  }
}

