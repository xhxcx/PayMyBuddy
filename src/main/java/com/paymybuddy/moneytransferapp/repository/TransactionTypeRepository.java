package com.paymybuddy.moneytransferapp.repository;

import com.paymybuddy.moneytransferapp.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
}
