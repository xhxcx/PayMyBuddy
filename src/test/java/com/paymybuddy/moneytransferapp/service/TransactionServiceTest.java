package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.constants.FeeRate;
import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.repository.BankAccountRepository;
import com.paymybuddy.moneytransferapp.repository.TransactionRepository;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.paymybuddy.moneytransferapp.model.TransactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private PMBUtils pmbUtilsMock;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction existingTransaction = new Transaction();
    private UserAccount sender = new UserAccount();
    private UserAccount beneficiary = new UserAccount();
    private List<Transaction> transactionList = new ArrayList<>();
    private BankAccount bankAccount = new BankAccount();
    private List<BankAccount> bankAccountList = new ArrayList<>();

    @BeforeEach
    public void setUpPerTest(){
        sender.setId(1);
        sender.setAccountBalance(BigDecimal.valueOf(100));
        sender.setFirstName("tyler");

        beneficiary.setId(2);
        beneficiary.setAccountBalance(BigDecimal.valueOf(0));
        beneficiary.setFirstName("toto");

        existingTransaction.setSender(sender);
        existingTransaction.setTransactionType(CONTACT_TRANSFER_PAYMENT);
        existingTransaction.setBeneficiary(beneficiary);
        existingTransaction.setDescription("test");
        existingTransaction.setAmount(10);

        transactionList.add(existingTransaction);

        sender.setTransactionListAsSender(transactionList);

        bankAccount.setId(1);
        bankAccount.setUser(sender);
        bankAccount.setIban("iban");
        bankAccount.setHolderName("holderName");
        bankAccount.setCaption("caption");

        bankAccountList.add(bankAccount);
        sender.setBankAccountList(bankAccountList);

    }

    @Test
    public void prepareNewContactTransactionTest(){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(sender);
        transactionDTO.setBeneficiary(new UserAccount());
        transactionDTO.setAmount(100);
        transactionDTO.setDescription("test");
        transactionDTO.setTransactionType(CONTACT_TRANSFER_PAYMENT);

        Transaction resultTransaction = transactionService.prepareNewTransaction(transactionDTO);

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
        assertThat(resultTransaction.getTransactionType().equals(CONTACT_TRANSFER_PAYMENT)).isTrue();

        verify(transactionRepositoryMock,Mockito.times(1)).save(any(Transaction.class));
    }

    @Test
    public void processTransactionForContactPaymentErrorAccountBalanceInsufficientTest(){
        Transaction tooHighAmountTransaction = new Transaction();
        tooHighAmountTransaction.setSender(sender);
        tooHighAmountTransaction.setBeneficiary(beneficiary);
        tooHighAmountTransaction.setTransactionType(CONTACT_TRANSFER_PAYMENT);
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
        transactionWithoutBeneficiary.setTransactionType(CONTACT_TRANSFER_PAYMENT);
        transactionWithoutBeneficiary.setAmount(10);

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(transactionWithoutBeneficiary));
        assertThat(exception.getMessage()).contains("Beneficiary not found");
    }

    @Test
    public void processTransactionForContactPaymentErrorSenderUnknownTest() {
        Transaction transactionWithoutSender = new Transaction();
        transactionWithoutSender.setSender(new UserAccount());
        transactionWithoutSender.setTransactionType(CONTACT_TRANSFER_PAYMENT);
        transactionWithoutSender.setAmount(10);

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(transactionWithoutSender));
        assertThat(exception.getMessage()).contains("Sender not found in database");
    }

    @Test
    public void processTransactionForBankDepositSuccessTest() throws PMBTransactionException {

        Transaction bankDeposit = new Transaction();
        bankDeposit.setBankAccount(bankAccount);
        bankDeposit.setFeeRate(FeeRate.BANK_TRANSFER_DEPOSIT_FEE_RATE.getValue());
        bankDeposit.setTransactionType(BANK_TRANSFER_DEPOSIT);
        bankDeposit.setAmount(10);
        bankDeposit.setBeneficiary(sender);
        bankDeposit.setSender(sender);
        bankDeposit.setDescription("deposit unit test");

        UserAccount updatedUser = sender;
        updatedUser.setAccountBalance(BigDecimal.valueOf(sender.getAccountBalance().doubleValue()+(bankDeposit.getAmount())-(0.5*bankDeposit.getAmount())));

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(bankAccountRepositoryMock.findAll()).thenReturn(bankAccountList);
        when(bankAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(bankAccount));

        when(userAccountRepositoryMock.save(sender)).thenReturn(updatedUser);

        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(bankDeposit);

        Transaction resultTransaction = transactionService.processTransaction(bankDeposit);

        assertThat(resultTransaction.getSender().equals(sender)).isTrue();
        assertThat(resultTransaction.getBeneficiary().equals(sender)).isTrue();
        assertThat(resultTransaction.getTransactionType().equals(BANK_TRANSFER_DEPOSIT)).isTrue();

        verify(transactionRepositoryMock,Mockito.times(1)).save(any(Transaction.class));
    }

    @Test
    public void processTransactionForBankDepositErrorBankAccountNotFoundTest() {

        Transaction bankDeposit = new Transaction();
        bankDeposit.setBankAccount(bankAccount);
        bankDeposit.setFeeRate(FeeRate.BANK_TRANSFER_DEPOSIT_FEE_RATE.getValue());
        bankDeposit.setTransactionType(BANK_TRANSFER_DEPOSIT);
        bankDeposit.setAmount(10);
        bankDeposit.setBeneficiary(sender);
        bankDeposit.setSender(sender);
        bankDeposit.setDescription("deposit unit test");

        UserAccount updatedUser = sender;
        updatedUser.setAccountBalance(BigDecimal.valueOf(sender.getAccountBalance().doubleValue()+(bankDeposit.getAmount())-(0.5*bankDeposit.getAmount())));

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(bankAccountRepositoryMock.findAll()).thenReturn(bankAccountList);

        when(userAccountRepositoryMock.save(sender)).thenReturn(updatedUser);

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(bankDeposit));
        assertThat(exception.getMessage()).contains("No bank account found for this transaction");

        verify(transactionRepositoryMock,Mockito.times(0)).save(any(Transaction.class));
    }

    @Test
    public void processTransactionForBankWithdrawalSuccessTest() throws PMBTransactionException {

        Transaction bankWithdraw = new Transaction();
        bankWithdraw.setBankAccount(bankAccount);
        bankWithdraw.setFeeRate(FeeRate.BANK_TRANSFER_WITHDRAWAL_FEE_RATE.getValue());
        bankWithdraw.setTransactionType(BANK_TRANSFER_WITHDRAWAL);
        bankWithdraw.setAmount(10);
        bankWithdraw.setBeneficiary(sender);
        bankWithdraw.setSender(sender);
        bankWithdraw.setDescription("withdraw unit test");

        UserAccount updatedUser = sender;
        updatedUser.setAccountBalance(BigDecimal.valueOf(sender.getAccountBalance().doubleValue()-(bankWithdraw.getAmount())+(0.5*bankWithdraw.getAmount())));

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(bankAccountRepositoryMock.findAll()).thenReturn(bankAccountList);
        when(bankAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(bankAccount));

        when(userAccountRepositoryMock.save(sender)).thenReturn(updatedUser);

        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(bankWithdraw);

        Transaction resultTransaction = transactionService.processTransaction(bankWithdraw);

        assertThat(resultTransaction.getSender().equals(sender)).isTrue();
        assertThat(resultTransaction.getBeneficiary().equals(sender)).isTrue();
        assertThat(resultTransaction.getTransactionType().equals(BANK_TRANSFER_WITHDRAWAL)).isTrue();

        verify(transactionRepositoryMock,Mockito.times(1)).save(any(Transaction.class));
    }

    @Test
    public void processTransactionForContactPaymentErrorUpdateSenderTest() {
        UserAccount badUpdateUser = new UserAccount();
        badUpdateUser.setId(36);

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(userAccountRepositoryMock.findById(2)).thenReturn(java.util.Optional.ofNullable(beneficiary));

        when(userAccountRepositoryMock.save(sender)).thenReturn(badUpdateUser);
        when(userAccountRepositoryMock.save(beneficiary)).thenReturn(beneficiary);

        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(existingTransaction);

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(existingTransaction));
        assertThat(exception.getMessage()).contains("Update on sender UserAccount error");

        verify(transactionRepositoryMock,Mockito.times(0)).save(any(Transaction.class));
    }

    @Test
    public void processTransactionForContactPaymentErrorUpdateBeneficiaryTest() {
        UserAccount badUpdateUser = new UserAccount();
        badUpdateUser.setId(36);

        when(userAccountRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(sender));
        when(userAccountRepositoryMock.findById(2)).thenReturn(java.util.Optional.ofNullable(beneficiary));

        when(userAccountRepositoryMock.save(sender)).thenReturn(sender);
        when(userAccountRepositoryMock.save(beneficiary)).thenReturn(badUpdateUser);

        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(existingTransaction);

        Exception exception = assertThrows(PMBTransactionException.class, () -> transactionService.processTransaction(existingTransaction));
        assertThat(exception.getMessage()).contains("Update on beneficiary UserAccount error");

        verify(transactionRepositoryMock,Mockito.times(0)).save(any(Transaction.class));
    }

    @Test
    public void getTransactionsAsPageTest(){
        Pageable pageable = PageRequest.of(0,1);
        Page<Transaction> expectedPage = new PageImpl<>(transactionList, pageable, transactionList.size());
        when((Page<Transaction>)pmbUtilsMock.transformListIntoPage(pageable,transactionList)).thenReturn(expectedPage);
        Page<Transaction> resultPage = transactionService.getTransactionsAsPage(PageRequest.of(0, 1),transactionList);

        assertThat(resultPage).isEqualTo(expectedPage);
    }
}
