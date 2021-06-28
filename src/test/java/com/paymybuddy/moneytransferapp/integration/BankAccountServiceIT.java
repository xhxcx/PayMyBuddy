package com.paymybuddy.moneytransferapp.integration;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.BankAccountRepository;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import com.paymybuddy.moneytransferapp.service.BankAccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@Sql({"/sql/DDL_tests.sql", "/sql/Data_test.sql"})
public class BankAccountServiceIT {

    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void addNewBankAccountIT(){
        UserAccount user = userAccountRepository.findUserAccountByEmail("test@gmail.com");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setCaption("caption");
        bankAccount.setIban("iban");
        bankAccount.setHolderName("holderName");
        bankAccount.setUser(user);

        BankAccount result = bankAccountService.addNewBankAccount(bankAccount);
        List<BankAccount> bankAccountList = bankAccountRepository.findBankAccountsByUser(user);

        assertThat(result).isEqualTo(bankAccount);
        assertThat(bankAccountList.get(1).getIban().equalsIgnoreCase("iban")).isTrue();
    }
}
