package com.banque.comptes.dto;

import com.banque.comptes.Entities.AccountType;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;

@Getter
@Setter
public class AccountRequestDTO {

    @NotNull
    private UUID clientID;

    @NotNull
    private AccountType accountType;

}
