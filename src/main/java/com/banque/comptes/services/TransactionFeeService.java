package com.banque.comptes.services;


import com.banque.comptes.Entities.Fee;
import com.banque.comptes.Entities.TransactionFee;
import com.banque.comptes.repository.TransactionFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TransactionFeeService {

    @Autowired
    private TransactionFeeRepository transactionFeeRepository;

    public TransactionFee createTransactionFee(Fee fee, Map<String,Double> transactionFeePercentage) {
        TransactionFee transactionFee = new TransactionFee();
        transactionFee.setFee(fee);
        transactionFee.setTransactionFeePercentage(transactionFeePercentage);

        return transactionFeeRepository.save(transactionFee);
    }

    public TransactionFee getTransactionFeeByFeeId(UUID feeId) {
        return transactionFeeRepository.findAll().stream().filter(transactionFee ->
                transactionFee.getFee().getId().equals(feeId)).findFirst().orElse(null);
    }

    public TransactionFee getTransactionFeeById(UUID id) {
        return transactionFeeRepository.findById(id).orElse(null);
    }
}
