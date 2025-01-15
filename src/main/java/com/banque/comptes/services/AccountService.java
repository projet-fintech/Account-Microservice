package com.banque.comptes.services;

import com.banque.comptes.Entities.Account;
import com.banque.comptes.repository.AccountRepository;
import com.banque.events.AccountCreatedEvent;
import com.banque.events.dto.AccountDto;
import com.banque.events.enums.AccountStatus;
import com.banque.events.enums.AccountType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KafkaTemplate<String,AccountCreatedEvent> kafkaTemplate;

    @Autowired
    private FeeService feeService;

    @Autowired
    private ReplyingKafkaTemplate<String, AccountDto,AccountDto> replyingKafkaTemplate;

    @Value("${account.fee.silver}")
    private double silverFee;

    @Value("${account.fee.gold}")
    private double goldFee;

    @Value("${account.fee.titanium}")
    private double titaniumFee;

    @Value("${spring.kafka.topic.account.created-topic}")
    private String accountCreatedTopic;


    public Account createAccount(UUID clientId, AccountType accountType) {
        Account account = new Account();
        account.setId_client(clientId);
        account.setAccountType(accountType);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(new Date());
        account.setBalance(0.0);
        account.setAccountNumber(generateAccountNumber());
//        feeService.createFee(account,calculateMonthlyFee(accountType));
        Account savedAccount =  accountRepository.save(account);

        // Publish AccountCreatedEvent
        AccountCreatedEvent accountCreatedEventevent = new AccountCreatedEvent();
        accountCreatedEventevent.setAccountId(savedAccount.getId_account());
        accountCreatedEventevent.setClientId(clientId);
        accountCreatedEventevent.setAccountNumber(savedAccount.getAccountNumber());
        kafkaTemplate.send(accountCreatedTopic, accountCreatedEventevent);

        return savedAccount;
    }

    public Account getAccountById(UUID accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(() ->
                new AccountNotFoundException("Account not found with id: " + accountId));
    }

    public void updateAccountStatus(UUID accountId, AccountStatus status) throws AccountNotFoundException {
        Account account = getAccountById(accountId);
        account.setStatus(status);
        accountRepository.save(account);
    }

    public void updateAccountBalance(UUID accountId, double amount) throws AccountNotFoundException {
        Account account = getAccountById(accountId);
        account.setBalance(amount);
        accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccount(UUID accountId) throws AccountNotFoundException {
        Account account = getAccountById(accountId);
        accountRepository.delete(account);
    }

    public List<Account> getAccountsByClientId(UUID clientId) {
        return accountRepository.findAll().stream()
                .filter(account -> account.getId_client().equals(clientId))
                .toList();
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 12);
    }

    @KafkaListener(topics = "${spring.kafka.request.topic}",groupId = "account-group") // This ensures request-reply
    public void handleAccountDetailsRequest(ConsumerRecord<String,UUID> record) throws AccountNotFoundException {
        //Extract account id from the record
        UUID accountId = record.value();
        Account account = getAccountById(accountId);
        AccountDto accountDto = new AccountDto(account.getId_account(),account.getId_client(),account.getAccountNumber(),account.getBalance(),account.getStatus(),account.getAccountType(),account.getCreatedAt());

        ProducerRecord<String, AccountDto> producerRecord = new ProducerRecord<>(
                record.headers().lastHeader("kafka_replyTopic").value() != null ? new String(record.headers().lastHeader("kafka_replyTopic").value()) : "account-details-reply-topic",
                accountDto
        );

        producerRecord.headers().add(record.headers().lastHeader("kafka_correlationId"));
        replyingKafkaTemplate.send(producerRecord);
    }
    @KafkaListener(topics = "${spring.kafka.topic.account-update-topic}", groupId = "account-group")
    public void handleAccountUpdate(ConsumerRecord<String,AccountDto> record) throws AccountNotFoundException{
        AccountDto accountDto = record.value();
        Account account = getAccountById(accountDto.getId_account());
        account.setBalance(accountDto.getBalance());
        accountRepository.save(account);
    }

    public double calculateMonthlyFee(AccountType accountType) {
        return switch (accountType) {
            case SILVER -> silverFee;
            case GOLD -> goldFee;
            case TITANIUM -> titaniumFee;
            default -> 0.0;
        };
    }

}