package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {
    Contact createContactBetweenUsers(String userEmail, String contactEmail, String alias);

    Page<Contact> getContactsAsPage(Pageable pageable, List<Contact> contactList);
}
