package com.paymybuddy.moneytransferapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BankAccountControllerTest {


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
        mockMvc.perform(post("/addBankAccount")
                .param("caption","captionTest")
                .param("iban", "ibanTest")
                .param("holderName", "holderNameTest")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bankAccount"));
    }
}
