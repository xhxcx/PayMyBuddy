package com.paymybuddy.moneytransferapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="transaction_type")
public class TransactionType {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name = "name")
    private String name;

}
