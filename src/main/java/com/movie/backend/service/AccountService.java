package com.movie.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.backend.entity.Account;
import com.movie.backend.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean login(String username, String password) {
        Optional<Account> accountOptional = accountRepository.findByUsernameAndPassword(username, password);
        return accountOptional.isPresent();
    }
}
