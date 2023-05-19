package com.example.accountsservices;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.TransactionException;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.IAccountsService;
import com.example.accountsservices.service.IBeneficiaryService;
import com.example.accountsservices.service.ITransactionsService;

import java.util.List;
import java.util.Optional;

//DESIGN NOTE
//Obeying I of SOLID ,to not pollute a single interface with too much methods
// by splitting it up.
//we want to have a single parent of all service classes so need a class to provide
// dummy implementation of all abstract methods of all interface
// so that we can spilt up service logic into separate concerning classes
// and thus obeying S of SOLID also

//since there is no need of instantiating the AbstractAccountsService ,
// it's just to provide dummy implementation ,so make it abstract
// it can be used for loose coupling or having some logic that will be used by all three service classes

public abstract class AbstractAccountsService implements IAccountsService, ITransactionsService, IBeneficiaryService {
    private  final  AccountsRepository accountsRepository;
    private  final CustomerRepository customerRepository;
    private  static final Accounts.AccountStatus STATUS_BLOCKED= Accounts.AccountStatus.BLOCKED;

    protected AbstractAccountsService(AccountsRepository accountsRepository,
                                      CustomerRepository customerRepository){
        this.accountsRepository=accountsRepository;
        this.customerRepository=customerRepository;
    }
    public OutputDto postRequestExecutor(PostInputRequestDto postInputDto) throws AccountsException, CustomerException { return null;}
    public OutputDto  putRequestExecutor(PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException { return null;}
    public OutputDto getRequestExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException { return null;}
    public OutputDto deleteRequestExecutor(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException{ return null;}

    //ben
    public OutputDto postRequestBenExecutor(PostInputRequestDto postInputDto) throws BeneficiaryException, AccountsException {return null;};
    public OutputDto putRequestBenExecutor(PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException { return null;};
    public OutputDto getRequestBenExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException { return null;};
    public OutputDto deleteRequestBenExecutor(DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException {return null;};

    //transactions
    public TransactionsDto transactionsExecutor(TransactionsDto transactionsDto) throws  TransactionException , AccountsException { return null;}
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount( Long accountNumber) throws AccountsException {return null;}
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
