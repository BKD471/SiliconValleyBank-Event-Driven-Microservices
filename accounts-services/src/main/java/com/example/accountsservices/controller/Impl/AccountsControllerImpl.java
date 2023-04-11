package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.controller.IAccountsController;
import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountsControllerImpl extends AbstractParentController implements IAccountsController{
    private final AccountsServiceImpl accountsService;

    AccountsControllerImpl(AccountsServiceImpl accountsService) {
        this.accountsService = accountsService;
    }


    /**
     * @param accountsDto
     * @return
     */
    @Override
    public ResponseEntity<AccountsDto> createAccounts(AccountsDto accountsDto) {
        AccountsDto createdAccounts=accountsService.createAccounts(accountsDto);
        return new ResponseEntity<>(createdAccounts, HttpStatus.CREATED);
    }

    /**
     * @param accountsDto
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<AccountsDto> updateAccount(AccountsDto accountsDto) throws AccountsException {
        AccountsDto updateAccount=accountsService.updateAccountDetails(accountsDto);
        return new ResponseEntity<>(updateAccount,HttpStatus.ACCEPTED);
    }

    /**
     * @param customerId
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<List<AccountsDto>> getAllAccountsByCustomerId(Long customerId) throws AccountsException {
        List<AccountsDto> listOfAccountsForACustomer=accountsService.getAllActiveAccountsByCustomerId(customerId);
        return new ResponseEntity<>(listOfAccountsForACustomer,HttpStatus.OK);
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<AccountsDto> getAccountInformation(Long accountNumber) throws AccountsException {
        AccountsDto accountInfo=accountsService.getAccountInfo(accountNumber);
        return new ResponseEntity<>(accountInfo,HttpStatus.OK);
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<String> deleteAccount(Long accountNumber) throws AccountsException {
        accountsService.deleteAccount(accountNumber);
        return new ResponseEntity<>(String.format("Account with id %s has been deleted",accountNumber),HttpStatus.ACCEPTED);
    }

    /**
     * @param customerId
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<String> deleteAllAccountsByCustomer(Long customerId) throws AccountsException {
        accountsService.deleteAllAccountsByCustomer(customerId);
        return new ResponseEntity<>(String.format("All accounts of customer with id %s has been deleted",customerId),HttpStatus.ACCEPTED);
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<String> blockAccount(Long accountNumber) throws AccountsException {
        accountsService.blockAccount(accountNumber);
        return new ResponseEntity<>(String.format("Account with id %s has been blocked",accountNumber),HttpStatus.ACCEPTED);
    }
}
