package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BankAccountDTO {
    private int id;

    private String caption;

    @NotNull
    @NotEmpty
    private String iban;

    @NotNull
    @NotEmpty
    private String holderName;

    @NotNull
    private UserAccount user;

}
