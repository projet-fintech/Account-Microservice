package com.banque.comptes.scheduled;



import com.banque.comptes.Entities.Account;
import com.banque.comptes.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MonthlyFeeScheduler {

    @Autowired
    private AccountService accountService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of every month
    public void calculateMonthlyFees() {
        LocalDate today = LocalDate.now();
        System.out.println("start job" +today);
        List<Account> accounts = accountService.getAllAccounts();

        accounts.forEach(account -> {
            double feeAmount = accountService.getFeeAmount(account.getAccountType());
            if (feeAmount > 0.0) {
                Double oldBalance = account.getBalance();
                account.setBalance(oldBalance - feeAmount);
                //publish to kafka
                String message = "fee payment for account " + account.getId_account() + " amount: " + feeAmount;
                kafkaTemplate.send("fee-payement-topics", message);
            }
        });
    }
}
