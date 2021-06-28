package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage BankAccounts
 */
@Service
public interface BankAccountService {

    /**
     * Get all BankAccount from a user
     *
     * @param user UserAccount from who we need the bank accounts
     * @return a List containing all bank accounts for the given user
     */
    List<BankAccount> getBankAccountsByUser(UserAccount user);

    /**
     * Save new BankAccount in database
     *
     * @param bankAccount BankAccount to save
     * @return BankAccount saved bank account
     */
    BankAccount addNewBankAccount(BankAccount bankAccount);
}
