package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.service.ContactService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactControllerTest {

    @MockBean
    private ContactService contactServiceMock;

    @MockBean
    private UserAccountService userAccountServiceMock;

    @Autowired
    private MockMvc mockMvc;

    private Contact contact = new Contact();
    private UserAccount user = new UserAccount();
    private UserAccount contactUser = new UserAccount();

    @BeforeEach
    public void setUpPerTests(){

        user.setEmail("tyler.durden@gmail.com");
        user.setFirstName("tyler");
        user.setLastName("durden");
        contactUser.setEmail("contact.email@mail.com");

        contact.setId(1);
        contact.setUser(user);
        contact.setContactUser(contactUser);
        contact.setAlias("alias");

    }

    @Test
    public void showAddContactFormTest() throws Exception {
        mockMvc.perform(get("/addContact"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_contact_form"));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com",password = "mdpTest")
    public void createContactTest() throws Exception {

        when(userAccountServiceMock.findUserByEmail("contact.email@mail.com")).thenReturn(contactUser);
        when(contactServiceMock.createContactBetweenUsers("tyler.durden@gmail.com","contact.email@mail.com","alias")).thenReturn(contact);

        mockMvc.perform(post("/addContact")
                .param("contactEmail","contact.email@mail.com")
                .param("alias","alias")
                .requestAttr("currentUser",user)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(username = "tyler.durden@gmail.com",password = "mdpTest")
    public void createContactWhenContactUserDoesntExistTest() throws Exception {

        when(userAccountServiceMock.findUserByEmail("contact.email@mail.com")).thenReturn(null);

        mockMvc.perform(post("/addContact")
                .param("contactEmail","contact.email@mail.com")
                .param("alias","alias")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add_contact_form"))
                .andExpect(model().attribute("contactEmail",contactUser.getEmail()))
                .andExpect(model().attribute("alias","alias"))
                .andExpect(model().attribute("emailErrorMessage","No user found for this email"));

        verify(contactServiceMock, Mockito.times(0)).createContactBetweenUsers(anyString(),anyString(),anyString());
    }
}