package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TransactionDTO {

    @NotNull
    private UserAccount sender;

    @NotNull
    private UserAccount beneficiary;

    @NotNull
    @Min(1)
    private double amount;

    @NotEmpty
    @NotNull
    private String description;
}
