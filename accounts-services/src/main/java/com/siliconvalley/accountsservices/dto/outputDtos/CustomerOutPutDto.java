package com.siliconvalley.accountsservices.dto.outputDtos;


import java.time.LocalDate;

public record CustomerOutPutDto(String customerId,String customerName,String email,
LocalDate DateOfBirth,int age,String phoneNumber,String adharNumber,String panNumber
,String voterId,String drivingLicense,String passportNumber,String address,String imageName){
    public static final class Builder{
        private String customerId;
        private String customerName;
        private String email;
        private LocalDate DateOfBirth;
        private int age;
        private String phoneNumber;
        private String adharNumber;
        private String panNumber;
        private String voterId;
        private String drivingLicense;
        private String passportNumber;
        private String address;
        private String imageName;

        public Builder(){}

        public Builder customerId(String customerId){
            this.customerId=customerId;
            return this;
        }
        public Builder customerName(String customerName){
            this.customerName=customerName;
            return this;
        }
        public Builder email(String email){
            this.email=email;
            return this;
        }
        public Builder DateOfBirth(LocalDate DateOfBirth){
            this.DateOfBirth=DateOfBirth;
            return this;
        }
        public Builder age(int age){
            this.age=age;
            return this;
        }
        public Builder phoneNumber(String phoneNumber){
            this.phoneNumber=phoneNumber;
            return this;
        }
        public Builder adharNumber(String adharNumber){
            this.adharNumber=adharNumber;
            return this;
        }
        public Builder panNumber(String panNumber){
            this.panNumber=panNumber;
            return this;
        }

        public Builder voterId(String voterId){
            this.voterId=voterId;
            return this;
        }
        public Builder drivingLicense(String drivingLicense){
            this.drivingLicense=drivingLicense;
            return this;
        }

        public Builder passportNumber(String passportNumber){
            this.passportNumber=passportNumber;
            return this;
        }
        public Builder address(String address){
            this.address=address;
            return this;
        }
        public Builder imageName(String imageName){
            this.imageName=imageName;
            return this;
        }
        public CustomerOutPutDto build(){
            return new CustomerOutPutDto(customerId,customerName,email,DateOfBirth,age,phoneNumber,
                    adharNumber,panNumber,voterId,drivingLicense,passportNumber,address,imageName);
        }
    }
}


