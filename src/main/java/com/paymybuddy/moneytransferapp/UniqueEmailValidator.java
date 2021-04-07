package com.paymybuddy.moneytransferapp;

import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import com.paymybuddy.moneytransferapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Override
    public void initialize(UniqueEmail uniqueEmail) {
        System.out.println("Validation in progress");
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userAccountService.findUserByEmail(email) == null;
    }
}
