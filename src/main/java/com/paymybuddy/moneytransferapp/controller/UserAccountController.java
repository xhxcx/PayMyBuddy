package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserAccountController {

    @Autowired
    UserAccountService userAccountService;

    @GetMapping("/")
    public String viewHome(){
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String validateUserRegistration(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model){
        model.addAttribute("user", user);
        if(bindingResult.hasErrors()){
            if(bindingResult.hasFieldErrors("email"))
                model.addAttribute("emailMessage", "Email already exists as a user");
            return "register";
        }
        else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userAccountService.createUser(user);
        }
        return "register_success";
    }

}
