package com.example.accountsservices.service.Impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.mapper.AccountsMapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.repository.AccountsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AccountsServiceImpl {
   private AccountsRepository accountsRepository;
   AccountsServiceImpl(AccountsRepository accountsRepository){
       this.accountsRepository=accountsRepository;
   }

   public AccountsDto createAccount(@RequestBody AccountsDto accountsDto){
       Accounts acount= AccountsMapper.mapToAccounts(accountsDto);
       Accounts savedAccounts=accountsRepository.save(acount);
       return  AccountsMapper.mapToAccountsDto(savedAccounts);
   }

   public AccountsDto getAccount(Long id){
       Optional<Accounts> fetchedAccount= Optional.ofNullable(accountsRepository.findByCustomerId(id));
       return AccountsMapper.mapToAccountsDto(fetchedAccount.get());
   }
}
