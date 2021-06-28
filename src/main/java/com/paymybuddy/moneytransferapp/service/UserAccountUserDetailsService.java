package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.config.UserAccountUserDetails;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation to use users from database to log in the app
 */
public class UserAccountUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByEmail(s);
        if(userAccount == null)
            throw new UsernameNotFoundException("User not found");
        return new UserAccountUserDetails(userAccount);
    }
}
