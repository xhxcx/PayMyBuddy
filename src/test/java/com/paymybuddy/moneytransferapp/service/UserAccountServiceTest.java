package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import com.paymybuddy.moneytransferapp.model.dto.UserDtoMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepositoryMock;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private UserDTO userDTO;

    private UserAccount existingUserAccount;

    private List<UserAccount> userAccountList = new ArrayList<>();

    @BeforeEach
    private void setUpPerTest(){
        userDTO = new UserDTO();
        userDTO.setEmail("tyler.durden@test.com");
        userDTO.setFirstName("tyler");
        userDTO.setLastName("durden");
        userDTO.setAddress("25 rue du nord, 59000 Lille");
        userDTO.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

        existingUserAccount = UserDtoMapper.INSTANCE.userDtoToUser(userDTO);
        userAccountList.add(existingUserAccount);

    }

    @Nested
    @DisplayName("createUser tests")
    class CreateUserTests {
        @Test
        public void createUserTest() {
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setEmail("toto.test@test.com");
            newUserDTO.setFirstName("toto");
            newUserDTO.setLastName("test");
            newUserDTO.setAddress("25 rue de paris, 92000 Nanterre");
            newUserDTO.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

            UserAccount newUser = UserDtoMapper.INSTANCE.userDtoToUser(newUserDTO);

            when(userAccountRepositoryMock.findAll()).thenReturn(userAccountList);
            when(userAccountRepositoryMock.save(any())).thenReturn(newUser);
            assertThat(userAccountService.createUser(newUserDTO)).isEqualTo(newUserDTO);
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
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setEmail("tyler.durden@test.com");
            newUserDTO.setFirstName("toto");
            newUserDTO.setLastName("test");
            newUserDTO.setAddress("25 rue de paris, 92000 Nanterre");
            newUserDTO.setPassword(new BCryptPasswordEncoder().encode("motdepasse"));

            when(userAccountRepositoryMock.findAll()).thenReturn(userAccountList);
            when(userAccountRepositoryMock.findUserAccountByEmail("tyler.durden@test.com")).thenReturn(existingUserAccount);
            assertThat(userAccountService.createUser(newUserDTO)).isEqualTo(null);
            verify(userAccountRepositoryMock, Mockito.times(0)).save(any());
        }
    }

}
