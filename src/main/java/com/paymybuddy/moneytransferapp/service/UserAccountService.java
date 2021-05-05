package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserAccountService {

    UserAccount createUser(UserAccount newUser);

    UserAccount updateUser(UserAccount userToUpdate);

    List<UserAccount> getAllUsers();

    UserAccount findUserByEmail(String email);
}
