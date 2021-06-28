package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage Contacts
 */
@Service
public interface ContactService {
    /**
     * Save a new user in database.
     * Verify a user exists for the userEmail and a user also exists for the contactEmail
     * Verify if the contact is already existing in db between these 2 users
     * Save a new Contact in database
     *
     * @param userEmail String email from the loggedin user
     * @param contactEmail String email of the contact user
     * @param alias String description of the Contact to display on views
     * @return Contact saved Contact or null if verifying conditions are not ok
     */
    Contact createContactBetweenUsers(String userEmail, String contactEmail, String alias);

    /**
     * Transform the given contact list into a Page of Contact
     *
     * @param pageable Pageable
     * @param contactList List to transform
     * @return Page<Contact> containing the contacts from the list.
     */
    Page<Contact> getContactsAsPage(Pageable pageable, List<Contact> contactList);
}
