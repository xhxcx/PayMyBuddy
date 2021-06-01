package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.BankAccountDTO;
import com.paymybuddy.moneytransferapp.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BankAccountControllerTest {

    @MockBean
    private BankAccountService bankAccountServiceMock;


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com", password = "mdpTest")
    public void goToBankAccountsForCurrentUserTest() throws Exception{

        mockMvc.perform(get("/bankAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("bank_account"))
                .andExpect(model().attributeExists("currentUser"));
    }

    @Test
    public void goToBankAccountsAnonymouslyTest() throws Exception{

        mockMvc.perform(get("/bankAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeDoesNotExist("currentUser"));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com", password = "mdpTest")
    public void showAddBankAccountFormTest() throws Exception {
        mockMvc.perform(get("/addBankAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_bank_account"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attributeExists("newBankAccount"));
    }

    @Test
    public void showAddBankAccountFormAnonymouslyTest() throws Exception {
        mockMvc.perform(get("/addBankAccount"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("dashboard"))
                .andExpect(model().attributeDoesNotExist("currentUser"))
                .andExpect(model().attributeDoesNotExist("newBankAccount"));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com", password = "mdpTest")
    public void addBankAccountTest() throws Exception {
        UserAccount user = new UserAccount();
        user.setId(1);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setUser(user);
        bankAccount.setIban("ibanTest");
        bankAccount.setHolderName("holderNameTest");
        bankAccount.setCaption("captionTest");

        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setId(1);
        bankAccountDTO.setUser(user);
        bankAccountDTO.setIban("ibanTest");
        bankAccountDTO.setHolderName("holderNameTest");
        bankAccountDTO.setCaption("captionTest");
        when(bankAccountServiceMock.addNewBankAccount(any(BankAccount.class))).thenReturn(bankAccount);

        mockMvc.perform(post("/addBankAccount")
                .flashAttr("newBankAccount",bankAccountDTO)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bankAccount"));
    }
}
