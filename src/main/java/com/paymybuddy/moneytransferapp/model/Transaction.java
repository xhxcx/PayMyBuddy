package com.paymybuddy.moneytransferapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "description")
    private String description;

    @Column(name = "fee_rate")
    private double feeRate;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private UserAccount sender;

    @ManyToOne
    @JoinColumn(name = "beneficiary_user_id")
    private UserAccount beneficiary;

    @OneToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
}
