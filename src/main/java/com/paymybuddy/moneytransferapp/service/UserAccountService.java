package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserAccountService {

    UserDTO createUser(UserDTO newUser);

    UserDTO updateUser(UserDTO userToUpdate);

    List<UserDTO> getAllUsers();

    UserDTO findUserByEmail(String email);
}
