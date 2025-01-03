package com.banque.comptes.dto;

import lombok.Data;
import org.hibernate.annotations.SecondaryRow;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
public class FeeResponseDTO {
    private UUID id;
    private UUID accountId;
    private double feeAmount;
    private LocalDateTime calculatedAt;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
