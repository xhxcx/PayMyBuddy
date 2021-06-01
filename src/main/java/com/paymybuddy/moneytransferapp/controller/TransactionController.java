package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(UserAccountController.class);

    @GetMapping(path = "/transfer")
    private String goToTransferDashboard(Model model){
        model.addAttribute("transactionDTO", new TransactionDTO());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String validateTransfer(@Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO, BindingResult bindingResult, Model model){
        model.addAttribute("transactionDTO", transactionDTO);
        if(bindingResult.hasErrors()){
            if(bindingResult.hasFieldErrors("amount")) {
                model.addAttribute("amountMessage", "Amount should be superior to 1 and with 2 decimal");
            }
            return (transactionDTO.getTransactionType().equals(TransactionType.CONTACT_TRANSFER_PAYMENT)) ? "transfer" : "bank_account";
        }
        else {
            Transaction transaction = transactionService.prepareNewTransaction(transactionDTO);
            logger.info("New transaction : " + transaction);
            try {
                transactionService.processTransaction(transaction);
            } catch (PMBTransactionException e) {
                //TODO affiner le message en fonction du message de l'exception levée
                e.printStackTrace();
                model.addAttribute("amountMessage", "Your sold is not sufficient for this transaction amount");
                return (transactionDTO.getTransactionType().equals(TransactionType.CONTACT_TRANSFER_PAYMENT)) ? "transfer" : "bank_account";
            }
        }
        return "transfer_success";
    }
}
