package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.service.TransactionService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionServiceMock;

    @MockBean
    private UserAccountService userAccountServiceMock;

    @Autowired
    private MockMvc mockMvc;

    private UserAccount currentUser = new UserAccount();
    private List<Transaction> transactionList = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        currentUser.setEmail("tyler.durden@gmail.com");
        currentUser.setId(1);
        currentUser.setFirstName("tyler");
        currentUser.setLastName("durden");
        currentUser.setAccountBalance(BigDecimal.valueOf(1000));

        Transaction existingTransaction = new Transaction();
        existingTransaction.setSender(currentUser);
        existingTransaction.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        existingTransaction.setBeneficiary(new UserAccount());
        existingTransaction.setDescription("test");
        existingTransaction.setAmount(10);

        transactionList.add(existingTransaction);
        currentUser.setTransactionListAsSender(transactionList);

    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com",password = "mdpTest")
    public void goToTransferDashboardTest() throws Exception {
        when(userAccountServiceMock.findUserByEmail("tyler.durden@gmail.com")).thenReturn(currentUser);
        mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("transactionDTO"))
                .andExpect(model().attribute("currentUser", currentUser));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com",password = "mdpTest")
    public void ValidateContactTransferSuccessTest() throws Exception {

        UserAccount beneficiary = new UserAccount();
        beneficiary.setId(2);
        beneficiary.setAccountBalance(BigDecimal.valueOf(0));
        beneficiary.setFirstName("toto");

        Transaction transaction = new Transaction();
        transaction.setSender(currentUser);
        transaction.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        transaction.setBeneficiary(beneficiary);
        transaction.setDescription("transaction2");
        transaction.setAmount(10);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(currentUser);
        transactionDTO.setBeneficiary(beneficiary);
        transactionDTO.setAmount(10);
        transactionDTO.setDescription("transaction2");

        when(userAccountServiceMock.findUserByEmail("tyler.durden@gmail.com")).thenReturn(currentUser);

        when(transactionServiceMock.prepareNewContactTransaction(transactionDTO)).thenReturn(transaction);
        when(transactionServiceMock.processTransaction(transaction)).thenReturn(transaction);

        mockMvc.perform(post("/transfer")
                .flashAttr("transactionDTO", transactionDTO)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer_success"))
                .andExpect(model().attribute("currentUser", currentUser));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com",password = "mdpTest")
    public void ValidateContactTransferAmountTooLowTest() throws Exception {

        UserAccount beneficiary = new UserAccount();
        beneficiary.setId(2);
        beneficiary.setAccountBalance(BigDecimal.valueOf(0));
        beneficiary.setFirstName("toto");

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(currentUser);
        transactionDTO.setBeneficiary(beneficiary);
        transactionDTO.setAmount(0.5);
        transactionDTO.setDescription("transaction with amount too low");

        when(userAccountServiceMock.findUserByEmail("tyler.durden@gmail.com")).thenReturn(currentUser);

        mockMvc.perform(post("/transfer")
                .flashAttr("transactionDTO", transactionDTO)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("currentUser", currentUser))
                .andExpect(model().attribute("amountMessage","Amount should be superior to 1 and with 2 decimal"));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com",password = "mdpTest")
    public void ValidateContactTransferAmountTooHighTest() throws Exception {

        UserAccount beneficiary = new UserAccount();
        beneficiary.setId(2);
        beneficiary.setAccountBalance(BigDecimal.valueOf(0));
        beneficiary.setFirstName("toto");

        Transaction transaction = new Transaction();
        transaction.setSender(currentUser);
        transaction.setTransactionType(TransactionType.CONTACT_TRANSFER_PAYMENT);
        transaction.setBeneficiary(beneficiary);
        transaction.setDescription("transaction amount too high for sender");
        transaction.setAmount(10000);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(currentUser);
        transactionDTO.setBeneficiary(beneficiary);
        transactionDTO.setAmount(10000);
        transactionDTO.setDescription("transaction amount too high for sender");

        when(userAccountServiceMock.findUserByEmail("tyler.durden@gmail.com")).thenReturn(currentUser);

        when(transactionServiceMock.prepareNewContactTransaction(transactionDTO)).thenReturn(transaction);
        when(transactionServiceMock.processTransaction(transaction)).thenThrow(PMBTransactionException.class);

        mockMvc.perform(post("/transfer")
                .flashAttr("transactionDTO", transactionDTO)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("currentUser", currentUser))
        .andExpect(model().attribute("amountMessage", "Your sold is not sufficient for this transaction amount"));
    }
}
