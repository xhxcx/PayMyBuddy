package com.paymybuddy.moneytransferapp.repository;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository <UserAccount, Integer>{

}
