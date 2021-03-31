package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.BankAccount;

import java.util.List;

public interface BankAccountService {

    List<BankAccount> getBankAccountsByUser(int userId);

    BankAccount addNewBankAccount(BankAccount bankAccount);

    BankAccount editBankAccount(int bankAccountId, BankAccount updatedBankAccount);

}
