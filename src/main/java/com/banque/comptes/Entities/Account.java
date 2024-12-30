package com.banque.comptes.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_account;

    @Column(nullable = false)
    private UUID id_client;

    private String accountNumber;

    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false,updatable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Fee> fees;

}
