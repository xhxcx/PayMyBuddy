package com.paymybuddy.moneytransferapp.service;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.repository.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService{

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private static final Logger logger = LogManager.getLogger(BankAccountService.class);

    @Override
    public List<BankAccount> getBankAccountsByUser(UserAccount user) {
        return bankAccountRepository.findBankAccountsByUser(user);
    }

    @Override
    public BankAccount addNewBankAccount(BankAccount bankAccount) {
        if (bankAccount == null){
            logger.error("Null bank account cannot be saved in database");
            return null;
        }
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount editBankAccount(int bankAccountId, BankAccount updatedBankAccount) {
        return null;
    }
}
