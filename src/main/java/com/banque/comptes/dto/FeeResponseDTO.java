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

}
