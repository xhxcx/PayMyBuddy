package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.exceptions.PMBTransactionException;
import com.paymybuddy.moneytransferapp.model.Transaction;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;
import com.paymybuddy.moneytransferapp.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LogManager.getLogger(UserAccountController.class);

    @GetMapping(path = "/transfer")
    private String goToTransferDashboard(Model model){
        model.addAttribute("transactionDTO", new TransactionDTO());
        model.addAttribute("activePage", "transfer");
        model.addAttribute("currentPage", "Transfer");
        this.paginationUpdate(model, 1,3);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String validateTransfer(@Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO, BindingResult bindingResult, Model model){
        model.addAttribute("transactionDTO", transactionDTO);
        paginationUpdate(model, 1,3);
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

    @GetMapping("/transactionList")
    private String getTransactionPage(Model model, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "3") int size){
        model.addAttribute("transactionDTO", new TransactionDTO());
        paginationUpdate(model, page, size);
        return "transfer";
    }

    private void paginationUpdate(Model model, int page, int size){
        List<Transaction> userTransactions = (List<Transaction>) model.getAttribute("transactionList");
        Page<Transaction> transactionPage = transactionService.getTransactionsAsPage(PageRequest.of(page -1, size),userTransactions);
        if(transactionPage.getTotalPages()>0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, transactionPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("transactionPage", transactionPage);
    }
}
