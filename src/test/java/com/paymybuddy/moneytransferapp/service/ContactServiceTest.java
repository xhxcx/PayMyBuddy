package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepositoryMock;

    @Mock
    private UserAccountService userAccountServiceMock;

    @Mock
    private PMBUtils pmbUtilsMock;

    @InjectMocks
    private ContactServiceImpl contactService;

    private Contact existingContact;
    private List<Contact> contactList = new ArrayList<>();

    @BeforeEach
    public void setUpForTests(){
        UserAccount currentUser = new UserAccount();
        UserAccount contactUser = new UserAccount();
        currentUser.setEmail("tyler.durden@gmail.com");
        contactUser.setEmail("contact.email@test.com");
        existingContact = new Contact();
        existingContact.setId(1);
        existingContact.setUser(currentUser);
        existingContact.setContactUser(contactUser);
        existingContact.setAlias("test");

        contactList.add(existingContact);
        currentUser.setContactList(contactList);

    }

    @Test
    public void createContactBetweenUsersTest(){
        UserAccount newUserContact = new UserAccount();
        newUserContact.setEmail("new.contact@test.com");
        when(userAccountServiceMock.findUserByEmail("tyler.durden@gmail.com")).thenReturn(existingContact.getUser());
        when(userAccountServiceMock.findUserByEmail("new.contact@test.com")).thenReturn(newUserContact);
        Contact resultContact = contactService.createContactBetweenUsers("tyler.durden@gmail.com","new.contact@test.com","test");
        verify(contactRepositoryMock, Mockito.times(1)).save(any());
        assertThat(resultContact.getContactUser().getEmail()).isEqualTo("new.contact@test.com");
    }

    @Test
    public void createContactBetweenUsersWhenContactAlreadyExistsTest(){
        when(userAccountServiceMock.findUserByEmail("tyler.durden@gmail.com")).thenReturn(existingContact.getUser());
        when(userAccountServiceMock.findUserByEmail("contact.email@test.com")).thenReturn(existingContact.getContactUser());

        Contact resultContact = contactService.createContactBetweenUsers("tyler.durden@gmail.com","contact.email@test.com","test");

        verify(contactRepositoryMock, Mockito.times(0)).save(any());
        assertThat(resultContact).isNull();
    }

    @Test
    public void getContactAsPageTest(){
        Pageable pageable = PageRequest.of(0,1);
        Page<Contact> expectedPage = new PageImpl<>(contactList, pageable, contactList.size());
        when((Page<Contact>)pmbUtilsMock.transformListIntoPage(pageable,contactList)).thenReturn(expectedPage);
        Page<Contact> resultPage = contactService.getContactsAsPage(PageRequest.of(0, 1),contactList);

        assertThat(resultPage).isEqualTo(expectedPage);
    }
}