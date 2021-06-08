package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.constants.FeeRate;
import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.repository.BankAccountRepository;
import com.paymybuddy.moneytransferapp.repository.TransactionRepository;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// TODO JAVADOC

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    private final UserAccountRepository userAccountRepository;

    private final BankAccountRepository bankAccountRepository;

    private final PMBUtils pmbUtils;

    @Autowired
    public TransactionServiceImpl(BankAccountRepository bankAccountRepository, UserAccountRepository userAccountRepository, TransactionRepository transactionRepository, PMBUtils utils) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userAccountRepository = userAccountRepository;
        this.pmbUtils = utils;
    }

    @Transactional
    @Override
    public Transaction prepareNewTransaction(TransactionDTO transactionDTO) {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.getAmount());
        newTransaction.setSender(transactionDTO.getSender());
        newTransaction.setBeneficiary(transactionDTO.getBeneficiary());
        newTransaction.setDescription(transactionDTO.getDescription());
        newTransaction.setDate(Timestamp.valueOf(LocalDateTime.now()));
        newTransaction.setTransactionType(transactionDTO.getTransactionType());
        String feeValue = transactionDTO.getTransactionType()+"_FEE_RATE";
        newTransaction.setFeeRate(FeeRate.valueOf(feeValue).getValue());
        newTransaction.setBankAccount(transactionDTO.getBankAccount());

        return newTransaction;
    }

    @Transactional(rollbackOn = PMBTransactionException.class)
    @Override
    public Transaction processTransaction(Transaction transaction) throws PMBTransactionException {
        Optional<UserAccount> sender = userAccountRepository.findById(transaction.getSender().getId());

        double amountForSender = calculateTransactionAmountForSender(transaction.getAmount(), transaction.getTransactionType());
        if(!sender.isPresent())
            return null;

        double newUserBalance = sender.get().getAccountBalance().doubleValue();

        if(!transaction.getTransactionType().equals(TransactionType.BANK_TRANSFER_DEPOSIT)) {
            if (transaction.getSender().getAccountBalance().doubleValue() < amountForSender)
                throw new PMBTransactionException("Sender sold is not sufficient for this transaction amount");
            newUserBalance = transaction.getSender().getAccountBalance().doubleValue() - amountForSender;
        }

        if (!transaction.getTransactionType().equals(TransactionType.CONTACT_TRANSFER_PAYMENT)){
            Optional<BankAccount> bankAccount = bankAccountRepository.findById(transaction.getBankAccount().getId());
            if (!bankAccount.isPresent())
                throw new PMBTransactionException("No bank account found for this transaction");
        }

        switch (transaction.getTransactionType()){
            case CONTACT_TRANSFER_PAYMENT:
                Optional<UserAccount> beneficiary = userAccountRepository.findById(transaction.getBeneficiary().getId());
                if (!beneficiary.isPresent())
                    throw new PMBTransactionException("Beneficiary not found in database");
                double newBeneficiaryBalance = beneficiary.get().getAccountBalance().doubleValue() + transaction.getAmount();
                beneficiary.get().setAccountBalance(BigDecimal.valueOf(newBeneficiaryBalance));

                UserAccount updatedBeneficiary = userAccountRepository.save(beneficiary.get());
                if (!updatedBeneficiary.getId().equals(beneficiary.get().getId()))
                    throw new PMBTransactionException("Update on beneficiary UserAccount error");
                break;

            case BANK_TRANSFER_DEPOSIT:
                newUserBalance = transaction.getSender().getAccountBalance().doubleValue() + amountForSender;
                break;

            case BANK_TRANSFER_WITHDRAWAL:
                break;

            default :
                throw new PMBTransactionException("Transaction type not allowed");
        }
        sender.get().setAccountBalance(BigDecimal.valueOf(newUserBalance));
        UserAccount updatedSender = userAccountRepository.save(sender.get());

        if (!updatedSender.getId().equals(sender.get().getId()))
            throw new PMBTransactionException("Update on sender UserAccount error");

        return transactionRepository.save(transaction);
    }

    private double calculateTransactionAmountForSender(double amount, TransactionType transactionType){
        double transactionAmountForSender = amount;
        switch (transactionType){
            case BANK_TRANSFER_WITHDRAWAL:
                transactionAmountForSender += amount*FeeRate.BANK_TRANSFER_WITHDRAWAL_FEE_RATE.getValue()/100;
                break;
            case BANK_TRANSFER_DEPOSIT:
                transactionAmountForSender -= amount*FeeRate.BANK_TRANSFER_DEPOSIT_FEE_RATE.getValue()/100;
                break;
            case CONTACT_TRANSFER_PAYMENT:
                transactionAmountForSender += amount*FeeRate.CONTACT_TRANSFER_PAYMENT_FEE_RATE.getValue()/100;
                break;
        }
        return transactionAmountForSender;
    }

    @Override
    public Page<Transaction> getTransactionsAsPage(Pageable pageable, List<Transaction> transactionList){
        return (Page<Transaction>) pmbUtils.transformListIntoPage(pageable, transactionList);
    }
}
