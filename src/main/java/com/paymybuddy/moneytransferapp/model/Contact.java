package com.paymybuddy.moneytransferapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "contact_alias")
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_user_id")
    private UserAccount contactUser;
}
