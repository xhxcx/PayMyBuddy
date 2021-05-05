package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserAccountService userAccountService;


    @Override
    public Contact createContactBetweenUsers(String userEmail, String contactEmail, String alias) {
        if(userEmail.equalsIgnoreCase(contactEmail))
            return null;

        UserAccount currentUser = userAccountService.findUserByEmail(userEmail);
        UserAccount contactUser = userAccountService.findUserByEmail(contactEmail);

        if(currentUser.getContactList().stream().anyMatch(contact -> contact.getContactUser().equals(contactUser)))
            return null;
        Contact contact = new Contact();
        contact.setUser(currentUser);
        contact.setContactUser(contactUser);
        contact.setAlias(alias);
        contactRepository.save(contact);
        return contact;
    }

}
