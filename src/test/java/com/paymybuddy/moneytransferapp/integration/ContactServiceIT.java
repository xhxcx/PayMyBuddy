package com.paymybuddy.moneytransferapp.integration;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.ContactRepository;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import com.paymybuddy.moneytransferapp.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@Sql({"/sql/DDL_tests.sql","/sql/Data_test.sql"})
public class ContactServiceIT {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void createContactBetweenUsersIT(){
        UserAccount currentUser = userAccountRepository.findById(1).orElse(new UserAccount());
        UserAccount contactUser = userAccountRepository.findById(4).orElse(new UserAccount());

        Contact newContact = new Contact();
        newContact.setAlias("createTestIT");
        newContact.setUser(currentUser);
        newContact.setContactUser(contactUser);

        contactService.createContactBetweenUsers(currentUser.getEmail(), contactUser.getEmail(), "createTestIT");

        Contact createdContact = contactRepository.findById(3).orElse(new Contact());
        assertThat(contactRepository.count()).isEqualTo(3);
        assertThat(createdContact.getAlias().equalsIgnoreCase("createTestIT"));
    }
}
