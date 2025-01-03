package com.banque.comptes.dto;

import com.banque.events.enums.AccountStatus;
import com.banque.events.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.UUID;


@Getter
@Setter
public class AccountResponseDTO {

    private UUID id_account;
    private UUID id_client;
    private String accountNumber;
    private Double balance;
    private AccountType accountType;
    private AccountStatus status;
    private Date createdAt;

    public UUID getId_account() {
        return id_account;
    }

    public void setId_account(UUID id_account) {
        this.id_account = id_account;
    }

    public UUID getId_client() {
        return id_client;
    }

    public void setId_client(UUID id_client) {
        this.id_client = id_client;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public AccountResponseDTO(UUID id_account, UUID id_client, String accountNumber, Double balance, AccountType accountType, AccountStatus status, Date createdAt) {
        this.id_account = id_account;
        this.id_client = id_client;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.createdAt = createdAt;
    }
}
