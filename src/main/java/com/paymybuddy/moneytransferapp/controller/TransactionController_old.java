/*
package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.service.TransactionService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class TransactionController_old {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping(path = "/transfer")
    private String goToTransferDashboard(Model model, Principal principal){
        System.out.println("CONTROLLER transfer");
        if(principal != null) {
            model.addAttribute("transactionDTO", new TransactionDTO());
            UserAccount currentUser = userAccountService.findUserByEmail(principal.getName());
            model.addAttribute("currentUser", currentUser);

            List<Contact> contactList = currentUser.getContactList();
            if(!contactList.isEmpty())
                model.addAttribute("contactList", contactList);

            List<Transaction> transactions = currentUser.getTransactionListAsSender();
            if(!transactions.isEmpty())
                model.addAttribute("transactionList", transactions);

            return "transfer";
        }
        return "index";
    }

    @PostMapping("/transfer")
    public String validateContactTransfer(@Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO, BindingResult bindingResult, Model model){
        model.addAttribute("transactionDTO", transactionDTO);
        System.out.println("MODEL CURRENTUSER :: " + model.getAttribute("currentUser"));
        System.out.println("\n\n\n\n\n\n\n" + transactionDTO + "\n" + transactionDTO.getSender().getFirstName() + "\n" + transactionDTO.getBeneficiary().getFirstName()+ "\n\n\n\n\n\n\n");
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldErrors());
            if(bindingResult.hasFieldErrors("amount")) {
                System.out.println("CATCH AMOUNT ERROR");
                model.addAttribute("amountMessage", "Amount should be superior to 1 and with 2 decimal");
            }
            return "redirect:/transfer";
        }
        else {
            Transaction transaction = transactionService.prepareNewContactTransaction(transactionDTO);
            try {
                transactionService.processTransaction(transaction);
            } catch (PMBTransactionException e) {
                e.printStackTrace();
                model.addAttribute("amountMessage", "Your sold is not sufficient for this transaction amount");
                return "redirect:/transfer";
            }
        }
        return "transfer_success";
    }
}
*/
