package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage Transactions
 */
@Service
public interface TransactionService {
    /**
     * Create a new Transaction from a TransactionDTO coming from controller
     * Add datetime to the transaction
     * Set fee rate regarding the TransactionDTO TransactionType
     * @param transactionDTO TransactionDTO
     * @return settled Transaction
     */
    Transaction prepareNewTransaction(TransactionDTO transactionDTO);

    /**
     * Transactional method to update users and transaction when the transaction is submitted.
     * Verify the TransactionType to process
     * Verify if the sender has enough money on his account
     * Calculate sender new balance
     * Calculate beneficiary new balance for the CONTACT_TRANSFER
     * Update sender and beneficiary
     * Save the transaction
     *
     * @param transaction Transaction to process
     * @return Transaction saved transaction or null if sender doesn't exist
     * @throws PMBTransactionException with explicit message if one of the verification is not ok
     */
    Transaction processTransaction(Transaction transaction) throws PMBTransactionException;

    /**
     * Transform the given transaction list into a Page of Transaction
     *
     * @param pageable Pageable
     * @param transactionList List to transform
     * @return Page<Transaction> containing the transactions from the list.
     */
    Page<Transaction> getTransactionsAsPage(Pageable pageable, List<Transaction> transactionList);
}
