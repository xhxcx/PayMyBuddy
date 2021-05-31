package com.paymybuddy.moneytransferapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="caption")
    private String caption;

    @Column(name="iban")
    private String iban;

    @Column(name="holder_name")
    private String holderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;
}
