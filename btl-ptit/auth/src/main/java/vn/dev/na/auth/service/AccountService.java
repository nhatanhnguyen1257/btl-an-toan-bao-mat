package vn.dev.na.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.dev.na.thcs.security.helper.Common;
import vn.dev.na.thcs.security.entitys.Account;
import vn.dev.na.thcs.security.repository.AccountRepository;
import vn.dev.na.thcs.security.request.SignupRequest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String userName) {
        return accountRepository.existsByUsername(userName);
    }

    public boolean existsByEmail(String userName) {
        return accountRepository.existsByEmail(userName);
    }

    public void save(SignupRequest signUpRequest) throws NoSuchAlgorithmException{
        Account account = new Account();
        account.setStatus(Common.Account.ACCOUNT_STATUS.OK.ordinal());
        account.setEmail(signUpRequest.getEmail());
        account.setUsername(signUpRequest.getUsername());
        account.setFullName(signUpRequest.getFullName());
        account.setCreateDate(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        accountRepository.save(account);
    }
}
