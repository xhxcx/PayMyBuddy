package com.paymybuddy.moneytransferapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_account")
public class UserAccount {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="address")
    private String address;

    @Column(name="account_balance")
    private float accountBalance;
}
