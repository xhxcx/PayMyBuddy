package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.BankAccountDTO;
import com.paymybuddy.moneytransferapp.model.dto.BankAccountDtoMapper;
import com.paymybuddy.moneytransferapp.service.BankAccountService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    private static final Logger logger = LogManager.getLogger(BankAccountController.class);

    @GetMapping("/bankAccount")
    public String goToBankAccountsForCurrentUser(Model model){
        UserAccount currentUser = getCurrentUserAsEntity(model);
        if (currentUser == null) {
            logger.info("Unknown current user, redirect to homepage");
            return "index";
        }
        logger.info("Current user : " + currentUser.toString() + " => go to bank account");
        return "bank_account";
    }

    @GetMapping("addBankAccount")
    public String showAddBankAccountForm(Model model){
        model.addAttribute("newBankAccount", new BankAccountDTO());
        if (model.getAttribute("currentUser") == null)
            return "redirect:dashboard";
        return "add_bank_account";
    }

    //TODO gérer par un objet DTO plutot que prendre x params ? pb pour add le currentUser en hidden à partir du front
    @PostMapping("addBankAccount")
//    public String addBankAccount(@Valid @ModelAttribute("newBankAccount")BankAccountDTO bankAccountDTO, BindingResult result, Model model){
    public String addBankAccount(@RequestParam String caption,@RequestParam String iban, @RequestParam String holderName, Model model){
        /*if(result.hasErrors()){
            System.out.println(result.getFieldErrors());
            model.addAttribute("bankAccountError", "Bank account informations are not ok to save");
            return "add_bank_account";
        }
        else{*/
            BankAccount bankAccountToAdd = new BankAccount();
            bankAccountToAdd.setCaption(caption);
            bankAccountToAdd.setIban(iban);
            bankAccountToAdd.setHolderName(holderName);
            bankAccountToAdd.setUser(getCurrentUserAsEntity(model));
            bankAccountService.addNewBankAccount(bankAccountToAdd);
        //}
        return "redirect:/bankAccount";
    }

    private UserAccount getCurrentUserAsEntity(Model model){
        return (UserAccount) model.getAttribute("currentUser");
    }


}
