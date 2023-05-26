package com.example.slotmachine;

public class SlotMachineModel {
    private int balance;
    private int betAmount;


    public SlotMachineModel(int balance, int betAmount) {
        this.balance = balance;
        this.betAmount = betAmount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBetAmount() {
        return betAmount;
    }


}
