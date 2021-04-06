package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import com.paymybuddy.moneytransferapp.model.dto.UserDtoMapper;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService{

    @Autowired
    private UserAccountRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO newUser) {
        if(newUser != null){
            UserAccount toCreate = UserDtoMapper.INSTANCE.userDtoToUser(newUser);
            if(userRepository.findUserAccountByEmail(newUser.getEmail()) == null) {
                userRepository.save(toCreate);
                return newUser;
            }
            else
                return null;
        }
        else
            return null;
    }

    @Override
    public UserDTO updateUser(UserDTO userToUpdate) {
        if(userToUpdate != null){
            if(userRepository.findUserAccountByEmail(userToUpdate.getEmail()) != null){
                UserAccount userToModify = UserDtoMapper.INSTANCE.userDtoToUser(userToUpdate);
                userRepository.save(userToModify);
                return userToUpdate;
            }
            else
                return null;
        }
        else
            return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserAccount> userList = userRepository.findAll();

        return UserDtoMapper.INSTANCE.usersToUserDtoList(userList);
    }
}
