package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    Transaction prepareNewTransaction(TransactionDTO transactionDTO);

    Transaction processTransaction(Transaction transaction) throws PMBTransactionException;

    Page<Transaction> getTransactionsAsPage(Pageable pageable, List<Transaction> transactionList);
}
