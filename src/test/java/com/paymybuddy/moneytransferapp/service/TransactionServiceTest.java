package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.repository.BankAccountRepository;
import com.paymybuddy.moneytransferapp.repository.TransactionRepository;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @Mock
    private UserAccountRepository userAccountRepositoryMock;

    @Mock
    private BankAccountRepository bankAccountRepositoryMock;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction existingTransaction = new Transaction();
    private UserAccount sender = new UserAccount();
    private  UserAccount beneficiary = new UserAccount();

    @BeforeEach
    public void setUpPerTest(){
        sender.setId(1);
        sender.setAccountBalance(BigDecimal.valueOf(100));
        sender.setFirstName("tyler");

        beneficiary.setId(2);
        beneficiary.setAccountBalance(BigDecimal.valueOf(0));
        beneficiary.setFirstName("toto");

        existingTransaction.setSender(sender);
        existingTransaction.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        existingTransaction.setBeneficiary(beneficiary);
        existingTransaction.setDescription("test");
        existingTransaction.setAmount(10);
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(existingTransaction);

        sender.setTransactionListAsSender(transactionList);

    }

    @Test
    public void prepareNewContactTransactionTest(){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(sender);
        transactionDTO.setBeneficiary(new UserAccount());
        transactionDTO.setAmount(100);
        transactionDTO.setDescription("test");

        Transaction resultTransaction = transactionService.prepareNewContactTransaction(transactionDTO);

        assertThat(resultTransaction.getSender().equals(sender)).isTrue();
        assertThat(resultTransaction.getAmount() == 100).isTrue();
        assertThat(resultTransaction.getDescription().equalsIgnoreCase("test")).isTrue();
    }

    @Test
    public void processTransactionForContactPaymentSuccessTest() throws PMBTransactionException {

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(userAccountRepositoryMock.findById(2)).thenReturn(java.util.Optional.ofNullable(beneficiary));

        when(userAccountRepositoryMock.save(sender)).thenReturn(sender);
        when(userAccountRepositoryMock.save(beneficiary)).thenReturn(beneficiary);

        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(existingTransaction);

        Transaction resultTransaction = transactionService.processTransaction(existingTransaction);

        assertThat(resultTransaction.getSender().equals(sender)).isTrue();
        assertThat(resultTransaction.getTransactionType().equals(TransactionType.CONTACT_TRANSFER_PAYMENT)).isTrue();

        verify(transactionRepositoryMock,Mockito.times(1)).save(any(Transaction.class));
    }

    @Test
    public void processTransactionForContactPaymentErrorAccountBalanceInsufficientTest(){
        Transaction tooHighAmountTransaction = new Transaction();
        tooHighAmountTransaction.setSender(sender);
        tooHighAmountTransaction.setBeneficiary(beneficiary);
        tooHighAmountTransaction.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        tooHighAmountTransaction.setAmount(10000);

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(userAccountRepositoryMock.findById(2)).thenReturn(java.util.Optional.ofNullable(beneficiary));

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(tooHighAmountTransaction));
        assertThat(exception.getMessage()).contains("sold is not sufficient");
    }

    @Test
    public void processTransactionForContactPaymentErrorBeneficiaryUnknownTest() {
        Transaction transactionWithoutBeneficiary = new Transaction();
        transactionWithoutBeneficiary.setSender(sender);
        transactionWithoutBeneficiary.setBeneficiary(new UserAccount());
        transactionWithoutBeneficiary.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        transactionWithoutBeneficiary.setAmount(10);

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(transactionWithoutBeneficiary));
        assertThat(exception.getMessage()).contains("Beneficiary not found");
    }

    @Test
    public void processTransactionForContactPaymentErrorSenderUnknownTest() throws PMBTransactionException {
        Transaction transactionWithoutSender = new Transaction();
        transactionWithoutSender.setSender(new UserAccount());
        transactionWithoutSender.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        transactionWithoutSender.setAmount(10);

        Transaction resultTransaction =  transactionService.processTransaction(transactionWithoutSender);
        assertThat(resultTransaction).isNull();
    }
}
