package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.Contact;
import org.springframework.stereotype.Service;

@Service
public interface ContactService {
    Contact createContactBetweenUsers(String userEmail, String contactEmail, String alias);
}
