package com.paymybuddy.moneytransferapp.integration;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.TransactionRepository;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import com.paymybuddy.moneytransferapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@Sql({"/sql/DDL_tests.sql", "/sql/Data_test.sql"})
public class TransactionServiceIT {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private int initialTransactionCount;

    private double beneficiaryInitialBalance;

    private Transaction newTransaction;

    @BeforeEach
    public void setUp(){
        initialTransactionCount = transactionRepository.findAll().size();

        UserAccount beneficiary = userAccountRepository.findUserAccountByEmail("jack.sparrow@mail.com");
        beneficiaryInitialBalance = beneficiary.getAccountBalance().doubleValue();

        newTransaction = new Transaction();
        newTransaction.setBeneficiary(beneficiary);
        newTransaction.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        newTransaction.setDescription("IT test success");
        newTransaction.setAmount(10);
        newTransaction.setFeeRate(0.5);
        newTransaction.setDate(Timestamp.valueOf(LocalDateTime.now()));
        newTransaction.setBankAccount(null);
    }

    @Test
    public void processTransactionSuccessIT() throws PMBTransactionException {
        UserAccount sender = userAccountRepository.findUserAccountByEmail("robb.fynn@mh.com");

        newTransaction.setSender(sender);

        transactionService.processTransaction(newTransaction);
        List<Transaction> transactionList = transactionRepository.findAll();
        UserAccount updatedBeneficiary = userAccountRepository.findUserAccountByEmail("jack.sparrow@mail.com");
        assertThat(transactionList.size()).isEqualTo(initialTransactionCount + 1);
        assertThat(transactionList.stream().anyMatch(transaction -> transaction.getDescription().equalsIgnoreCase("IT test success"))).isTrue();
        assertThat(updatedBeneficiary.getAccountBalance().doubleValue()).isEqualTo(beneficiaryInitialBalance+newTransaction.getAmount());
    }

    @Test
    public void processTransactionRollbackDueToUnknownSenderAsUserIT() {
        UserAccount fakeSender = new UserAccount();
        fakeSender.setId(999);
        fakeSender.setEmail("do.not@exist.com");

        newTransaction.setSender(fakeSender);

        assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(newTransaction));
        List<Transaction> transactionList = transactionRepository.findAll();
        UserAccount beneficiaryAfterProcess = userAccountRepository.findUserAccountByEmail("jack.sparrow@mail.com");
        assertThat(transactionList.size()).isEqualTo(initialTransactionCount);
        assertThat(transactionList.stream().anyMatch(transaction -> transaction.getDescription().equalsIgnoreCase("IT test success"))).isFalse();
        assertThat(beneficiaryAfterProcess.getAccountBalance().doubleValue()).isEqualTo(beneficiaryInitialBalance);
    }

}
