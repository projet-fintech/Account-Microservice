package com.banque.comptes.controller;


import com.banque.comptes.Entities.Account;
import com.banque.comptes.Entities.AccountStatus;
import com.banque.comptes.dto.AccountRequestDTO;
import com.banque.comptes.dto.AccountResponseDTO;
import com.banque.comptes.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        Account account = accountService.CreateAccount(accountRequestDTO.getClientID(), accountRequestDTO.getAccountType());
        return new ResponseEntity<>(new
                AccountResponseDTO(account.getId_account(),account.getId_client(),account.getAccountNumber(), account.getBalance(),account.getAccountType(),account.getStatus(),account.getCreatedAt()), HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable UUID accountId) throws AccountNotFoundException {
        Account account = accountService.getAccountById(accountId);
        return new ResponseEntity<>(new
                AccountResponseDTO(account.getId_account(),account.getId_client(),account.getAccountNumber(), account.getBalance(),account.getAccountType(),account.getStatus(),account.getCreatedAt()), HttpStatus.OK);
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<String> updateAccountStatus(@PathVariable UUID accountId, @RequestParam AccountStatus status) throws AccountNotFoundException {
        accountService.updateAccountStatus(accountId,status);
        return new ResponseEntity<>("Status updated successfully",HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponseDTO> accountResponseDtos = accounts.stream().map(account -> new
                AccountResponseDTO(account.getId_account(),account.getId_client(),account.getAccountNumber(), account.getBalance(), account.getAccountType(),account.getStatus(),account.getCreatedAt())).toList();
        return new ResponseEntity<>(accountResponseDtos,HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsByClientId(@PathVariable UUID clientId) {
        List<Account> accounts = accountService.getAccountsByClientId(clientId);
        List<AccountResponseDTO> accountResponseDtos = accounts.stream().map(account -> new
                AccountResponseDTO(account.getId_account(),account.getId_client(),account.getAccountNumber(), account.getBalance(), account.getAccountType(),account.getStatus(),account.getCreatedAt())).toList();
        return new ResponseEntity<>(accountResponseDtos,HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID accountId) throws AccountNotFoundException {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>("Account deleted successfully",HttpStatus.OK);
    }
}
