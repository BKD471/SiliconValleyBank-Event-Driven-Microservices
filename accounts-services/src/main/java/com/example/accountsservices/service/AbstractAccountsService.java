package com.example.accountsservices.service;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.*;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;



public abstract class AbstractAccountsService implements IAccountsService, ITransactionsService, IBeneficiaryService,IFileService {
    private  final  AccountsRepository accountsRepository;
    private  final CustomerRepository customerRepository;
    private  static final Accounts.AccountStatus STATUS_BLOCKED= Accounts.AccountStatus.BLOCKED;

    protected AbstractAccountsService(AccountsRepository accountsRepository,
                                      CustomerRepository customerRepository){
        this.accountsRepository=accountsRepository;
        this.customerRepository=customerRepository;
    }
    public OutputDto postRequestExecutor(PostInputRequestDto postInputDto) throws AccountsException, CustomerException, IOException { return null;}
    public OutputDto  putRequestExecutor(PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException, IOException { return null;}
    public OutputDto getRequestExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException, IOException { return null;}
    public OutputDto deleteRequestExecutor(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException{ return null;}
    public OutputDto accountSetUp(PostInputRequestDto postInputRequestDto){return null;}
    //ben
    public OutputDto postRequestBenExecutor(PostInputRequestDto postInputDto) throws BeneficiaryException, AccountsException {return null;};
    public OutputDto putRequestBenExecutor(PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException { return null;};
    public OutputDto getRequestBenExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException { return null;};
    public OutputDto deleteRequestBenExecutor(DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException {return null;};

    //transactions
    public OutputDto transactionsExecutor(TransactionsDto transactionsDto) throws  TransactionException , AccountsException { return null;}
    public OutputDto getPastSixMonthsTransactionsForAnAccount( Long accountNumber) throws AccountsException {return null;}

    //file service
    public String uploadFile(MultipartFile image, String path) throws IOException {return null;}
    public InputStream getResource(String path, String name) throws FileNotFoundException {return null;}

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
