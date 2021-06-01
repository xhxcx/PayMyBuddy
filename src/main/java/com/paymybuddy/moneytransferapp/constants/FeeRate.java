package com.paymybuddy.moneytransferapp.constants;

public enum FeeRate {
    CONTACT_TRANSFER_PAYMENT_FEE_RATE(0.5),
    BANK_TRANSFER_DEPOSIT_FEE_RATE (0.5),
    BANK_TRANSFER_WITHDRAWAL_FEE_RATE (0.5);

    private final double value;

    FeeRate (double value){
        this.value = value;
    }
    public double getValue(){
        return value;
    }

}
