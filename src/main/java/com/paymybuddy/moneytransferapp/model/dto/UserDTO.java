package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.validators.UniqueEmail;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class UserDTO {

    private Integer id;

    @NotEmpty
    @NotNull
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String password;

    @UniqueEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String address;

    private BigDecimal accountBalance;

}
