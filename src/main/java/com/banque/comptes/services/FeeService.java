package com.banque.comptes.services;

import com.banque.comptes.Entities.Account;
import com.banque.comptes.Entities.Fee;
import com.banque.comptes.Entities.TransactionFee;
import com.banque.comptes.repository.FeeRepository;
import com.banque.comptes.repository.TransactionFeeRepository;
import com.banque.events.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private TransactionFeeRepository transactionFeeRepository;

    public Fee createFee(Account account, double monthlyFee) {
        Fee fee = new Fee();
        fee.setAccount(account);
        fee.setCalculatedAt(LocalDateTime.now());
        fee.setMonthlyFee(monthlyFee);
        TransactionFee transactionFee = new TransactionFee();
        Map<String, Double> transactionFeePercentage = new HashMap<>();
        transactionFeePercentage.put("WITHDRAW",0.01);
        transactionFeePercentage.put("TRANSFER", 0.005);
        transactionFeePercentage.put("BILL_PAYMENT",0.01);

        transactionFee.setTransactionFeePercentage(transactionFeePercentage);
        transactionFee.setFee(fee);
        transactionFeeRepository.save(transactionFee);
        fee.setTransactionFee(transactionFee);

        return feeRepository.save(fee);
    }

    public Fee getFeeByAccountType(AccountType accountType){
        return feeRepository.findAll().stream().filter(fee ->
                fee.getAccount().getAccountType().equals(accountType)).findFirst().orElse(null);
    }

    public Fee getFeeById(UUID id) {
        return feeRepository.findById(id).orElse(null);
    }
}
