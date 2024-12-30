package com.banque.comptes.dto;

import com.banque.comptes.Entities.AccountStatus;
import com.banque.comptes.Entities.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class AccountResponseDTO {

    private UUID id_account;
    private UUID id_client;
    private String accountNumber;
    private Double balance;
    private AccountType accountType;
    private AccountStatus status;
    private Date createdAt;
}
