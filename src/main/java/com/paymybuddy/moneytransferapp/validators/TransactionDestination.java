package com.paymybuddy.moneytransferapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = TransactionDestinationValidator.class)
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionDestination {

    String message() default "A transaction must have a beneficiary to send money or a bank account for bank transfer";

    Class[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
