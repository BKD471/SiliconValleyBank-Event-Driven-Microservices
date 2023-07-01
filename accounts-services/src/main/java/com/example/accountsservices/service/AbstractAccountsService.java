package com.example.accountsservices.service;

import com.example.accountsservices.exception.*;
import com.example.accountsservices.repository.IAccountsRepository;
import com.example.accountsservices.repository.ICustomerRepository;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;

import java.util.Optional;

import static com.example.accountsservices.helpers.AllEnumConstantHelpers.STATUS_BLOCKED;


public abstract class AbstractAccountsService {
    private  final IAccountsRepository accountsRepository;
    private  final ICustomerRepository customerRepository;

    protected AbstractAccountsService(IAccountsRepository accountsRepository,
                                      ICustomerRepository customerRepository){
        this.accountsRepository=accountsRepository;
        this.customerRepository=customerRepository;
    }


    protected Accounts fetchAccountByAccountNumber(Long accountNumber, String ...request) throws AccountsException {
        String methodName="fetchAccountByAccountNumber(Long,String vararg) in AbstractAccountsService";
        Optional<Accounts> fetchedAccounts = accountsRepository.findByAccountNumber(accountNumber);
        if (fetchedAccounts.isEmpty())
            throw new AccountsException(AccountsException.class,String.format("No such accounts exist with id %s", accountNumber),methodName);

        boolean checkAccountIsBlocked=STATUS_BLOCKED.equals(fetchedAccounts.get().getAccountStatus());
        if(request.length>0 && request[0].equalsIgnoreCase(AccountsServiceImpl.REQUEST_TO_BLOCK) && checkAccountIsBlocked) throw new AccountsException(AccountsException.class,String.format("Account of id %s is already blocked",accountNumber),methodName);
        else if(checkAccountIsBlocked) throw new AccountsException(AccountsException.class,String.format("Account of id %s is in %s status",accountNumber,STATUS_BLOCKED),methodName);
        return fetchedAccounts.get();
    }

    protected Customer fetchCustomerByCustomerNumber(Long customerId) throws CustomerException{
        String methodName="fetchCustomerByCustomerNumber(Long)";
        Optional<Customer> loadCustomer=customerRepository.findById(customerId);
        if(loadCustomer.isEmpty()) throw  new CustomerException(CustomerException.class,String.format("No such customer with id %s exist",customerId),
                methodName);
        return loadCustomer.get();
    }
}
