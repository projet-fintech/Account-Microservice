package com.banque.comptes.dto;

import com.banque.events.enums.AccountType;

import java.util.UUID;


public class AccountRequestDTO {

    private UUID id_client;


    private AccountType accountType;

    public UUID getId_client() {
        return id_client;
    }

    public void setId_client(UUID id_client) {
        this.id_client = id_client;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
