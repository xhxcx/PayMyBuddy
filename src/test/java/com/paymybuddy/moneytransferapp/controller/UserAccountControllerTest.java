package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.service.ContactService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserAccountControllerTest {

    @MockBean
    private UserAccountService userAccountServiceMock;

    @MockBean
    private ContactService contactServiceMock;

    @Autowired
    private MockMvc mockMvc;

    private static UserAccount user;
    private static List<UserAccount> userList;
    private static UserAccount contactUser;
    private static Contact contact =new Contact();
    private static List<Contact> contactList = new ArrayList<>();
    private static Page<Contact> contactPage;

    @BeforeAll
    public static void setUp(){
        userList = new ArrayList<>();
        user = new UserAccount();
        user.setEmail("tyler.durden@test.com");
        user.setFirstName("tyler");
        user.setLastName("durden");
        user.setAddress("25 rue du nord, 59000 Lille");
        user.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

        contactUser = new UserAccount();
        contactUser.setEmail("contact.user@test.com");
        contactUser.setFirstName("contact");
        contactUser.setLastName("user");
        contactUser.setAddress("contact address");
        contactUser.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

        userList.add(user);
        userList.add(contactUser);



        contact.setUser(user);
        contact.setContactUser(contactUser);
        contact.setAlias("contact");
        contactList.add(contact);

        user.setContactList(contactList);

        contactPage = new PageImpl<>(contactList, PageRequest.of(1, 1), contactList.size());

    }

    @Test
    public void viewHomeTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    @WithMockUser(username = "user.test",password = "mdpTest")
    public void viewHomeAlreadyLoggedTest() throws Exception {
        when(userAccountServiceMock.findUserByEmail(anyString())).thenReturn(user);
        when(contactServiceMock.getContactsAsPage(any(),eq(contactList))).thenReturn(contactPage);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("currentUser", user))
                .andExpect(model().attributeExists("contactPage"));
    }

    @Test
    public void showRegistrationFormTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("register"));
    }

    @Test
    public void validateUserRegistrationForAlreadyExistingEmailTest() throws Exception {
        when(userAccountServiceMock.getAllUsers()).thenReturn(userList);
        when(userAccountServiceMock.findUserByEmail(anyString())).thenReturn(user);
        String existingUser = "{\"firstName\":\"tyler\",\"lastName\":\"Durden\",\"email\":\"tyler.durden@test.com\",\"address\":\"25 rue du nord, 59000 Lille\",\"password\":\"password\"}";
        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(existingUser)
        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("emailMessage"))
                .andExpect(view().name("register"));

        verify(userAccountServiceMock, times(0)).createUser(any(UserAccount.class));
    }

    @Test
    public void validateUserRegistrationForNewEmailTest() throws Exception {
        when(userAccountServiceMock.getAllUsers()).thenReturn(userList);
        when(userAccountServiceMock.findUserByEmail("new.email@test.com")).thenReturn(null);
        String newUser = "{\"firstName\":\"new\",\"lastName\":\"user\",\"email\":\"new.email@test.com\",\"address\":\"2 rue de paris\",\"password\":\"password\"}";
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUser)
                .param("firstName","new")
                .param("lastName", "user")
                .param("email","new.email@test.com")
                .param("address", "2 rue de paris")
                .param("password", "motdepasse")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("register_success"));

        verify(userAccountServiceMock, times(1)).createUser(any(UserAccount.class));
    }

    @Test
    public void goToLoggedDashboardAsAnonymousTest() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user.test",password = "mdpTest")
    public void goToLoggedDashboardTest() throws Exception {
        when(userAccountServiceMock.findUserByEmail(anyString())).thenReturn(user);
        when(contactServiceMock.getContactsAsPage(any(),eq(contactList))).thenReturn(contactPage);
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attribute("currentUser",user))
                .andExpect(model().attribute("contactPage", contactPage));
    }

    @Test
    public void showLoginTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("currentUser"));
    }

    @Test
    @WithMockUser(username = "user.test",password = "mdpTest")
    public void getContactPageTest() throws Exception {
        when(userAccountServiceMock.findUserByEmail(anyString())).thenReturn(user);
        when(contactServiceMock.getContactsAsPage(any(),eq(contactList))).thenReturn(contactPage);
        mockMvc.perform(get("/contactList")
        .param("page", String.valueOf(1))
        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attribute("currentUser",user))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attribute("contactPage", contactPage));
    }
}
