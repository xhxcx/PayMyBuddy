package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankAccountService {

    List<BankAccount> getBankAccountsByUser(UserAccount user);

    BankAccount addNewBankAccount(BankAccount bankAccount);
}
