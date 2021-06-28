package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for UserAccount management
 */
@Service
public interface UserAccountService {

    /**
     * Save a new user in database.
     * Verify if the user is already existing in db before saving it
     * @param newUser user to save
     * @return UserAccount saved user or null if user already exists
     */
    UserAccount createUser(UserAccount newUser);

    /**
     * Get all users from db
     * @return a List containing all users in database, empty list if there is no user
     */
    List<UserAccount> getAllUsers();

    /**
     * Search user in db from an email
     * @param email String email to search
     * @return UserAccount found user, return null if no user found
     */
    UserAccount findUserByEmail(String email);
}
