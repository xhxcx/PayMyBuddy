package com.paymybuddy.moneytransferapp.model;

public enum TransactionType {
    BANK_TRANSFER_DEPOSIT ("Deposit"),
    BANK_TRANSFER_WITHDRAWAL ("Withdraw"),
    CONTACT_TRANSFER_PAYMENT ("Contact transfer");

    private final String value;
    TransactionType(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
