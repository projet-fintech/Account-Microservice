package com.banque.comptes.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;


@Getter
@Setter
public class FeeRequestDTO {

    @NotNull
    private UUID accountId;

    @NotNull
    private double feeAmount;
}
