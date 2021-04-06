package com.paymybuddy.moneytransferapp.config;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserAccountUserDetails implements UserDetails {

    private UserAccount userAccount;

    public UserAccountUserDetails(UserAccount userAccount){
        this.userAccount = userAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return userAccount.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getAuthenticateUserName(){
        return userAccount.getFirstName() + " " + userAccount.getLastName();
    }
}
