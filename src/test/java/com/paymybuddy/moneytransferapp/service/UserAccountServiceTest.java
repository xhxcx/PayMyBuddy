package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepositoryMock;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private UserAccount user;

    private List<UserAccount> userAccountList = new ArrayList<>();

    @BeforeEach
    private void setUpPerTest(){
        user = new UserAccount();
        user.setEmail("tyler.durden@test.com");
        user.setFirstName("tyler");
        user.setLastName("durden");
        user.setAddress("25 rue du nord, 59000 Lille");
        user.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));
        user.setAccountBalance(BigDecimal.ZERO);

        userAccountList.add(user);

    }

    @Nested
    @DisplayName("createUser tests")
    class CreateUserTests {
        @Test
        public void createUserTest() {
            UserAccount newUser = new UserAccount();
            newUser.setEmail("toto.test@test.com");
            newUser.setFirstName("toto");
            newUser.setLastName("test");
            newUser.setAddress("25 rue de paris, 92000 Nanterre");
            newUser.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

            UserAccount createdUser = userAccountService.createUser(newUser);

            when(userAccountRepositoryMock.findAll()).thenReturn(userAccountList);
            when(userAccountRepositoryMock.save(any(UserAccount.class))).thenReturn(new UserAccount());

            assertThat(createdUser).isEqualTo(newUser);
            verify(userAccountRepositoryMock, Mockito.times(1)).save(any());
        }

        @Test
        public void createUserWithNullTest() {
            when(userAccountRepositoryMock.findAll()).thenReturn(userAccountList);
            assertThat(userAccountService.createUser(null)).isEqualTo(null);
            verify(userAccountRepositoryMock, Mockito.times(0)).save(any());
        }

        @Test
        public void createUserWithAlreadyExistingEmailTest() {
            UserAccount newUser = new UserAccount();
            newUser.setEmail("tyler.durden@test.com");
            newUser.setFirstName("toto");
            newUser.setLastName("test");
            newUser.setAddress("25 rue de paris, 92000 Nanterre");
            newUser.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

            when(userAccountRepositoryMock.findAll()).thenReturn(userAccountList);
            when(userAccountRepositoryMock.findUserAccountByEmail("tyler.durden@test.com")).thenReturn(user);
            assertThat(userAccountService.createUser(newUser)).isEqualTo(null);
            verify(userAccountRepositoryMock, Mockito.times(0)).save(any());
        }
    }

    @Test
    public void findUserByEmailTest(){
        when(userAccountRepositoryMock.findUserAccountByEmail(anyString())).thenReturn(user);

        UserAccount foundUser = userAccountService.findUserByEmail("toto.test@test.com");

        assertThat(foundUser).isEqualTo(user);
    }

}
