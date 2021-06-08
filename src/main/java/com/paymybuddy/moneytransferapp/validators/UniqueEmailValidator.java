package com.paymybuddy.moneytransferapp.validators;

import com.paymybuddy.moneytransferapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserAccountRepository userAccountRepository;


    @Override
    public void initialize(UniqueEmail uniqueEmail) { }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userAccountRepository.findUserAccountByEmail(email) == null;
    }
}
