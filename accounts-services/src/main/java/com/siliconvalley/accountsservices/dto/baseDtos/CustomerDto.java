package com.siliconvalley.accountsservices.dto.baseDtos;


import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Set;

public record CustomerDto(String customerId,LocalDate DateOfBirth,String customerName,
                          int age,String email,String password,String phoneNumber,
                          String adharNumber,String panNumber,String voterId,
                          String drivingLicense,String passportNumber,
                          String address,String imageName,MultipartFile customerImage,
                         Set<AccountsDto> accounts,Set<RoleDto> roles){

    public CustomerDto withId(String customerId) {
        return new CustomerDto(customerId, DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withDateOfBirth(LocalDate DateOfBirth) {
        return new CustomerDto(customerId(), DateOfBirth, customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withCustomerName(String customerName) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName, age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withAge(int age) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age,
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withEmail(String email) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email,password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withPhoneNumber(String phoneNumber) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber,adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withAdharNumber(String adharNumber) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withPanNumber(String panNumber) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber,voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withVoterId(String voterId) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId,drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withDrivingLicense(String drivingLicense) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense,
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withPassportNumber(String passportNumber) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber,address(),imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withAddress(String address) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address,imageName(),
                customerImage(),accounts(),roles());
    }

    public CustomerDto withImageName(String imageName) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName,
                customerImage(),accounts(),roles());
    }

    public CustomerDto withCustomerImage(MultipartFile customerImage) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage,accounts(),roles());
    }

    public CustomerDto withAccounts(Set<AccountsDto> accounts) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts,roles());
    }

    public CustomerDto withRoles(Set<RoleDto> roles) {
        return new CustomerDto(customerId(), DateOfBirth(), customerName(), age(),
                email(),password(),phoneNumber(),adharNumber()
                ,panNumber(),voterId(),drivingLicense(),
                passportNumber(),address(),imageName(),
                customerImage(),accounts(),roles);
    }


    public static final class Builder{
        private String customerId;
        private LocalDate DateOfBirth;
        private String customerName;
        private int age;
        private String email;
        private String password;
        private String phoneNumber;
        private String adharNumber;
        private String panNumber;
        private String voterId;
        private String drivingLicense;
        private String passportNumber;
        private String address;
        private String imageName;
        private MultipartFile customerImage;
        private Set<AccountsDto> accounts;
        private Set<RoleDto> roles;

        public Builder(){
        }

        public Builder customerId(String customerId){
            this.customerId=customerId;
            return this;
        }

        public Builder DateOfBirth(LocalDate DateOfBirth){
            this.DateOfBirth=DateOfBirth;
            return this;
        }

        public Builder customerName(String customerName){
            this.customerName=customerName;
            return this;
        }

        public Builder age(int age){
            this.age=age;
            return this;
        }

        public Builder email(String email){
            this.email=email;
            return this;
        }

        public Builder password(String password){
            this.password=password;
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

        public Builder customerImage(MultipartFile customerImage){
            this.customerImage=customerImage;
            return this;
        }

        public Builder accounts(Set<AccountsDto> accounts){
            this.accounts=accounts;
            return this;
        }

        public Builder roles(Set<RoleDto> roles){
            this.roles=roles;
            return this;
        }

        public CustomerDto build(){
            return new CustomerDto(customerId, DateOfBirth, customerName,
             age, email, password, phoneNumber,adharNumber, panNumber, voterId, drivingLicense, passportNumber, address,
                    imageName,customerImage,accounts, roles);
        }
    }
}

