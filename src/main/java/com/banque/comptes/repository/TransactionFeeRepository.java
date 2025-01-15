package com.banque.comptes.repository;

import com.banque.comptes.Entities.TransactionFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionFeeRepository extends JpaRepository<TransactionFee, UUID> {
}
