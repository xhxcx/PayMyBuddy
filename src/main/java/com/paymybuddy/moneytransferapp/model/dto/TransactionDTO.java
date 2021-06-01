package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import com.paymybuddy.moneytransferapp.model.TransactionType;
import com.paymybuddy.moneytransferapp.model.UserAccount;
import com.paymybuddy.moneytransferapp.validators.TransactionDestination;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@TransactionDestination
@Data
public class TransactionDTO {

    @NotNull
    private UserAccount sender;

    private UserAccount beneficiary;

    @NotNull
    @Min(1)
    private double amount;

    @NotEmpty
    @NotNull
    private String description;

    private BankAccount bankAccount;

    @NotNull
    private TransactionType transactionType;
}
