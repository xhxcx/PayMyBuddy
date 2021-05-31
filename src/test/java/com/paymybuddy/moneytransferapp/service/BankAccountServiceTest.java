package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepositoryMock;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private final List<BankAccount> tylerBankAccounts = new ArrayList<>();
    private final UserAccount user = new UserAccount();

    @BeforeEach
    public void setUp(){
        user.setFirstName("test");

        BankAccount existingBankAccount = new BankAccount();
        existingBankAccount.setId(1);
        existingBankAccount.setCaption("caption");
        existingBankAccount.setIban("iban");
        existingBankAccount.setHolderName("holderName");
        existingBankAccount.setUser(user);

        tylerBankAccounts.add(existingBankAccount);
    }

    @Test
    public void getBankAccountsByUserShouldReturnAListOfBankAccount(){
        when(bankAccountRepositoryMock.findBankAccountsByUser(user)).thenReturn(tylerBankAccounts);
        List<BankAccount> result = bankAccountService.getBankAccountsByUser(user);
        verify(bankAccountRepositoryMock, Mockito.times(1)).findBankAccountsByUser(user);
        assertThat(result.get(0).getIban()).isEqualTo("iban");
    }

    @Test
    public void addNewBankAccountShouldReturnAddedAccountWhenAddSuccess(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setCaption("caption");
        bankAccount.setIban("iban");
        bankAccount.setHolderName("holderName");
        bankAccount.setUser(user);

        when(bankAccountRepositoryMock.save(bankAccount)).thenReturn(bankAccount);
        BankAccount result = bankAccountService.addNewBankAccount(bankAccount);

        verify(bankAccountRepositoryMock,Mockito.times(1)).save(bankAccount);
        assertThat(result).isEqualTo(bankAccount);
    }

    @Test
    public void addNewBankAccountShouldReturnNullAndNoCallToRepositoryWhenParamIsNull(){
        BankAccount result = bankAccountService.addNewBankAccount(null);

        verify(bankAccountRepositoryMock,Mockito.times(0)).save(any());
        assertThat(result).isNull();
    }
}
