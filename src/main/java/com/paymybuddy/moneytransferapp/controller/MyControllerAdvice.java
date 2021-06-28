package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.*;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class MyControllerAdvice {

    @Autowired
    private UserAccountService userAccountService;

    @ModelAttribute
    public void addUserInfoToModel(Principal principal, Model model) {
        UserAccount currentUser = null;
        if (principal != null) {
            currentUser = userAccountService.findUserByEmail(principal.getName());
        }
        model.addAttribute("currentUser",currentUser);


        if(currentUser != null) {
            List<Contact> contactList = currentUser.getContactList();
            if (!contactList.isEmpty())
                model.addAttribute("contactList", contactList);

            List<Transaction> transactions = currentUser.getTransactionListAsSender();
            if (!transactions.isEmpty())
                model.addAttribute("transactionList", transactions);

            List<BankAccount> bankAccounts = currentUser.getBankAccountList();
            if (!bankAccounts.isEmpty())
                model.addAttribute("bankAccounts", bankAccounts);
        }

        List<TransactionType> bankTransactionTypes = new ArrayList<>();
        bankTransactionTypes.add(TransactionType.BANK_TRANSFER_DEPOSIT);
        bankTransactionTypes.add(TransactionType.BANK_TRANSFER_WITHDRAWAL);
        model.addAttribute("transactionTypes", bankTransactionTypes);
    }
}
