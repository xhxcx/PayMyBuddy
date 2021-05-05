package com.paymybuddy.moneytransferapp.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ContactDTO {

    private Integer id;

    @NotNull
    private UserDTO user;

    @NotNull
    private UserDTO contactUser;

    @NotNull
    @NotEmpty
    private String alias;

}
