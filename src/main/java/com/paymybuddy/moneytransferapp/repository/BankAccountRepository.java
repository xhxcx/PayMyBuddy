package com.paymybuddy.moneytransferapp.repository;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findBankAccountsByUser(UserAccount user);
}
