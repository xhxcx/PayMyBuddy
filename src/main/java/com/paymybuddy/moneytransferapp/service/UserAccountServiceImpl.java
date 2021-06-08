package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService{

    private final UserAccountRepository userRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAccount createUser(UserAccount newUser) {
        if(newUser != null) {
            if (findUserByEmail(newUser.getEmail()) == null) {
                userRepository.save(newUser);
                return newUser;
            }
        }
        return null;
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserAccount findUserByEmail(String email) {
        return userRepository.findUserAccountByEmail(email);
    }
}
