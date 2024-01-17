package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.externalservice.service.input_validation_services.IEmailValidationService;
import com.siliconvalley.accountsservices.service.IIdentityValidationService;
import org.springframework.stereotype.Service;

@Service("IdentityServicePrimary")
public class IdentityServiceImpl implements IIdentityValidationService {
    private final IEmailValidationService emailValidationService;

    IdentityServiceImpl(IEmailValidationService emailValidationService){
        this.emailValidationService=emailValidationService;
    }
    /**
     * @return
     */
    @Override
    public boolean isValidEmail(String email) {
        //emailValidationService.validateEmail(email);
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean isValidPhone() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean isValidVoter() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean isValidPassport() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public boolean isValidPanCard() {
        return false;
    }
}
