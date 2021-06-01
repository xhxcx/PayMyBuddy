package com.paymybuddy.moneytransferapp.validators;

import com.paymybuddy.moneytransferapp.model.dto.TransactionDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransactionDestinationValidator implements ConstraintValidator<TransactionDestination, TransactionDTO> {
    @Override
    public void initialize(TransactionDestination constraintAnnotation) {

    }

    @Override
    public boolean isValid(TransactionDTO transactionDTO, ConstraintValidatorContext constraintValidatorContext) {
        return (transactionDTO.getBeneficiary() != null || transactionDTO.getBankAccount() != null);
    }
}
