package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(path = "/transfer")
    private String goToTransferDashboard(Model model){
        model.addAttribute("transactionDTO", new TransactionDTO());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String validateContactTransfer(@Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO, BindingResult bindingResult, Model model){
        model.addAttribute("transactionDTO", transactionDTO);
        if(bindingResult.hasErrors()){
            if(bindingResult.hasFieldErrors("amount")) {
                model.addAttribute("amountMessage", "Amount should be superior to 1 and with 2 decimal");
            }
            return "transfer";
        }
        else {
            Transaction transaction = transactionService.prepareNewContactTransaction(transactionDTO);
            try {
                transactionService.processTransaction(transaction);
            } catch (PMBTransactionException e) {
                //TODO affiner le message en fonction du message de l'exception levée
                e.printStackTrace();
                model.addAttribute("amountMessage", "Your sold is not sufficient for this transaction amount");
                return "transfer";
            }
        }
        return "transfer_success";
    }
}
