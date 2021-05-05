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

    @Autowired
    private UserAccountRepository userRepository;

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
    public UserAccount updateUser(UserAccount userToUpdate) {
        if(userToUpdate != null){
            if(userRepository.findUserAccountByEmail(userToUpdate.getEmail()) != null){
                userRepository.save(userToUpdate);
                return userToUpdate;
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
