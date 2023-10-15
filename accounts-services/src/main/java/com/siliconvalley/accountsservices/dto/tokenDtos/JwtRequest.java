package com.siliconvalley.accountsservices.dto.tokenDtos;

public record JwtRequest(String email,String password){
    public static final class Builder{
        private String email;
        private String password;

        public Builder(){}
        public Builder email(String email){
            this.email=email;
            return this;
        }

        public Builder password(String password){
            this.password=password;
            return this;
        }

        public JwtRequest build(){
            return new JwtRequest(email,password);
        }
    }
}
