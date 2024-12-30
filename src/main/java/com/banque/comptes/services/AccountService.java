package com.banque.comptes.services;

import com.banque.comptes.Entities.Account;
import com.banque.comptes.Entities.AccountStatus;
import com.banque.comptes.Entities.AccountType;
import com.banque.comptes.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Value("${account.fee.silver}")
    private double silverFee;

    @Value("${account.fee.gold}")
    private double goldFee;

    @Value("${account.fee.titanium}")
    private double titaniumFee;

    public Account CreateAccount(UUID clientId, AccountType accountType){
        Account account = new Account();
        account.setId_client(clientId);
        account.setAccountType(accountType);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(new Date());
        account.setBalance(0.0);
        account.setAccountNumber(generateAccountNumber());

        return accountRepository.save(account);
    }

    public Account getAccountById(UUID accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(() -> new
                AccountNotFoundException("Account not found with id" + accountId));
    }

    public void updateAccountStatus(UUID accountId, AccountStatus status) throws AccountNotFoundException {
        Account account = getAccountById(accountId);
        account.setStatus(status);
        accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccount(UUID accountId) throws AccountNotFoundException {
        Account account = getAccountById(accountId);
        accountRepository.delete(account);
    }

    public List<Account> getAccountsByClientId(UUID clientId) {
        return accountRepository.findAll().stream().filter(account ->
                account.getId_client().equals(clientId)).toList();
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 12);
    }

    @KafkaListener(topics = "statement-generated-topic", groupId = "account-service")
    public void handleStatementGenerated(String message) {
        System.out.println("message received " +message);
    }

    public double getFeeAmount(AccountType accountType) {
        return switch (accountType) {
            case SILVER -> silverFee;
            case GOLD -> goldFee;
            case TITANIUM -> titaniumFee;
            default -> 0.0;
        };
    }
}
