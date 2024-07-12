package com.movie.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.backend.entity.Account;
import com.movie.backend.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    
    private String username;
    

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account login(String username, String password) {
        Optional<Account> accountOptional = accountRepository.findByUsernameAndPassword(username, password);
        if (!accountOptional.isPresent()) {
        	return null;
        }
        return accountOptional.get();
    }
    
    public void createAccount(String username, String password) {
    	Integer newId = accountRepository.getMaxId() + 1 ;
    	
    	Account newAccount = new Account(newId, username, password);
    	
    	accountRepository.save(newAccount);
    }
}
