package com.paymybuddy.moneytransferapp.model;

import com.paymybuddy.moneytransferapp.UniqueEmail;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="user_account")
public class UserAccount {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @UniqueEmail
    @Column(name="email", unique = true, length = 254, nullable = false)
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
    private BigDecimal accountBalance = BigDecimal.ZERO;
}
