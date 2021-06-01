package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    Transaction prepareNewTransaction(TransactionDTO transactionDTO);

    Transaction processTransaction(Transaction transaction) throws PMBTransactionException;
}
