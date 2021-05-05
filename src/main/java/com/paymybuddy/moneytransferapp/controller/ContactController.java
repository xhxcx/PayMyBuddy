package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.service.ContactService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/addContact")
    public String showAddContactForm(Model model){
        model.addAttribute("contactEmail");
        model.addAttribute("alias");
        return "add_contact_form";
    }

    @PostMapping("/addContact")
    public String createContact(@RequestParam String contactEmail, @RequestParam String alias, Model model, Principal principal){
        model.addAttribute("alias", alias);
        model.addAttribute("contactEmail", contactEmail);
        UserAccount contactUser = userAccountService.findUserByEmail(contactEmail);
        Contact newContact =null;
        if(contactUser != null){
            newContact = contactService.createContactBetweenUsers(principal.getName(),contactUser.getEmail(),alias);
        }
        else
            model.addAttribute("emailErrorMessage","No user found for this email");

        if(newContact == null)
            model.addAttribute("errorMessage","Can not create contact with user : " + contactEmail);

        return (newContact !=null) ? "redirect:/dashboard" : "add_contact_form";
    }
}
