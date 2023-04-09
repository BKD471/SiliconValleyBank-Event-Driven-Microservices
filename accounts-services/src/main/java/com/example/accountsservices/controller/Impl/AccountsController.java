package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountsController extends AbstractParentController {
    private final AccountsServiceImpl accountsService;

    AccountsController(AccountsServiceImpl accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * @param accountsDto
     * @return
     */
    @Override
    public ResponseEntity<AccountsDto> createAccounts(AccountsDto accountsDto) {
        return null;
    }

    /**
     * @param accountsDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<AccountsDto> updateAccount(AccountsDto accountsDto) throws AccountsException {
        return null;
    }

    /**
     * @param customerId
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<List<AccountsDto>> getAllAccountsByCustomerId(Long customerId) throws AccountsException {
        return null;
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<AccountsDto> getAccountInformation(Long accountNumber) throws AccountsException {
        return null;
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<String> deleteAccount(Long accountNumber) throws AccountsException {
        return null;
    }

    /**
     * @param customerId
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<String> deleteAllAccountsByCustomer(Long customerId) throws AccountsException {
        return null;
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<String> blockAccount(Long accountNumber) throws AccountsException {
        return null;
    }
}
