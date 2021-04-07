package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class UserAccountControllerTest {

    @MockBean
    private UserAccountService userAccountServiceMock;

    @Autowired
    private MockMvc mockMvc;

    private static UserDTO userDTO;
    private static List<UserDTO> userList;

    @BeforeAll
    public static void setUp(){
        userList = new ArrayList<>();
        userDTO = new UserDTO();
        userDTO.setEmail("tyler.durden@test.com");
        userDTO.setFirstName("tyler");
        userDTO.setLastName("durden");
        userDTO.setAddress("25 rue du nord, 59000 Lille");
        userDTO.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

        userList.add(userDTO);

    }

    @Test
    public void viewHomeTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
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
        when(userAccountServiceMock.findUserByEmail(anyString())).thenReturn(userDTO);
        mockMvc.perform(post("/register")
        .param("email",userDTO.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("emailMessage"))
                .andExpect(view().name("register"));

        verify(userAccountServiceMock, times(0)).createUser(any(UserDTO.class));
    }

    @Test
    public void validateUserRegistrationForNewEmailTest() throws Exception {
        when(userAccountServiceMock.getAllUsers()).thenReturn(userList);
        when(userAccountServiceMock.findUserByEmail(anyString())).thenReturn(null);
        mockMvc.perform(post("/register")
                .param("email","new.email@test.com")
                .param("firstName","tyler")
                .param("lastName", "Durden")
                .param("address", "2 rue de paris")
                .param("password", "passwordTest"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("register_success"));

        verify(userAccountServiceMock, times(1)).createUser(any(UserDTO.class));
    }
}
