package com.siliconvalley.accountsservices.service;

public interface IIdentityValidationService {
    boolean isValidEmail(String email);
    boolean isValidPhone();
    boolean isValidVoter();
    boolean isValidPassport();
    boolean isValidPanCard();
}
