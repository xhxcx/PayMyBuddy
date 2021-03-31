package com.paymybuddy.moneytransferapp.repository;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
}
