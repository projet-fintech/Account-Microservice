package com.banque.comptes.scheduled;



import com.banque.comptes.Entities.Account;
import com.banque.comptes.Entities.Fee;
import com.banque.comptes.services.AccountService;
import com.banque.comptes.services.FeeService;
import com.banque.events.dto.AccountDto;
import com.banque.events.dto.WalletUpdateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


import java.time.LocalDate;
import java.util.List;

@Component
public class MonthlyFeeScheduler {

    @Autowired
    private AccountService accountService;

    @Autowired
    private FeeService feeService;

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of every month
    public void chargeMonthlyFees() {
        LocalDate today = LocalDate.now();
        System.out.println("start job" +today);
        List<Account> accounts = accountService.getAllAccounts();

//        accounts.forEach(account -> {
//            double feeAmount = accountService.getFeeAmount(account.getAccountType());
//            if (feeAmount > 0.0) {
//                Double oldBalance = account.getBalance();
//                account.setBalance(oldBalance - feeAmount);
//                //publish to kafka
//                String message = "fee payment for account " + account.getId_account() + " amount: " + feeAmount;
//                kafkaTemplate.send("fee-payement-topics", message);
//            }
//        });
        for (Account account : accounts) {
            Fee fee = feeService.getFeeByAccountType(account.getAccountType());
            if (fee == null) {
                System.out.println("Could not find fees for account " + account.getId_account());
                continue;
            }
            double monthlyFee = fee.getMonthlyFee();
            try {
                AccountDto accountDto = new AccountDto(
                        account.getId_account(),
                        account.getId_client(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getStatus(),
                        account.getAccountType(),
                        account.getCreatedAt()
                );
                if (accountDto.getBalance() < monthlyFee){
                    System.out.println("Not enough balance to pay the monthly fee for account "+ account.getId_account());
                    continue;
                }
                accountService.updateAccountBalance(account.getId_account(),account.getBalance() - monthlyFee);
                updateWallet(monthlyFee);
            } catch (Exception e) {
                System.out.println("Error while updating the monthly fee for account " + account.getId_account());
            }
        }
    }

    private void updateWallet(double amount) {
        String url = transactionServiceUrl + "/api/wallet/update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        WalletUpdateRequestDto requestBody = new WalletUpdateRequestDto(amount);
        HttpEntity<WalletUpdateRequestDto> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(url,request,String.class);
        } catch (Exception e) {
            System.out.println("Error while updating the bank wallet with REST: " + e.getMessage());
        }
    }
}
