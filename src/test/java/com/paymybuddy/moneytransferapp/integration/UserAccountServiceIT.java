package com.paymybuddy.moneytransferapp.integration;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@Sql("/sql/DDL_tests.sql")
public class UserAccountServiceIT {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Test
    public void createUserIT(){
        UserAccount newUser = new UserAccount();
        newUser.setFirstName("fName");
        newUser.setLastName("lName");
        newUser.setEmail("newUser.integration@test.com");
        newUser.setPassword("pwd");
        newUser.setAddress("address");
        newUser.setAccountBalance(BigDecimal.TEN);

        userAccountService.createUser(newUser);

        assertThat(userAccountRepository.findUserAccountByEmail("newUser.integration@test.com")).isNotNull();
    }
}
