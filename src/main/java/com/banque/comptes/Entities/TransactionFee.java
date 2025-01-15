package com.banque.comptes.Entities;

import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "transaction_fees")
public class TransactionFee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "fee_id")
    private Fee fee;

    @Column(nullable = false)
    @ElementCollection
    private Map<String, Double> transactionFeePercentage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public Map<String, Double> getTransactionFeePercentage() {
        return transactionFeePercentage;
    }

    public void setTransactionFeePercentage(Map<String, Double> transactionFeePercentage) {
        this.transactionFeePercentage = transactionFeePercentage;
    }
}
