package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
        String existingUser = "{\"firstName\":\"tyler\",\"lastName\":\"Durden\",\"email\":\"tyler.durden@test.com\",\"address\":\"25 rue du nord, 59000 Lille\",\"password\":\"password\"}";
        mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(existingUser)
        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("emailMessage"))
                .andExpect(view().name("register"));

        verify(userAccountServiceMock, times(0)).createUser(any(UserDTO.class));
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

        verify(userAccountServiceMock, times(1)).createUser(any(UserDTO.class));
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

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("principal"));
    }
}
