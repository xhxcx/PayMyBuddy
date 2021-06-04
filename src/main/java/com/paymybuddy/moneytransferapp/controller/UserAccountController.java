package com.paymybuddy.moneytransferapp.controller;

import com.paymybuddy.moneytransferapp.model.Contact;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.model.dto.UserDTO;
import com.paymybuddy.moneytransferapp.model.dto.UserDtoMapper;
import com.paymybuddy.moneytransferapp.service.ContactService;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    private static final Logger logger = LogManager.getLogger(UserAccountController.class);

    @GetMapping("/")
    public String viewHome(@ModelAttribute("currentUser")UserAccount currentUser, Model model){
        if(currentUser != null) {
            paginationUpdate(model,1,3);
            return "dashboard";
        }
        logger.info("Go to HP");
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(){
        return "login";
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
            userAccountService.createUser(userDtoMapper.userDtoToUser(user));
        }
        return "register_success";
    }

    @GetMapping(path = "/dashboard")
    private String goToLoggedDashboard(Model model, Principal principal){
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("currentPage", "Dashboard");
        if(principal != null) {
            paginationUpdate(model,1,3);
        }
        return "dashboard";
    }

    @GetMapping("/contactList")
    private String getContactPage(Model model, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "3") int size){
        paginationUpdate(model, page, size);
        return "dashboard";
    }

    private void paginationUpdate(Model model, int page, int size){
        List<Contact> userContacts = (List<Contact>) model.getAttribute("contactList");
        Page<Contact> contactPage = contactService.getContactsAsPage(PageRequest.of(page -1, size),userContacts);
        if(contactPage.getTotalPages()>0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, contactPage.getTotalPages()).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("contactPage", contactPage);
    }
}
