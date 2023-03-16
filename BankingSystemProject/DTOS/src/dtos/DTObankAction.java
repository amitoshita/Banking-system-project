package dtos;

import customers.CustomerBankAction;

public class DTObankAction {

    private int yazActionTime;
    private int amountOfMoney;
    private char sign;
    private int beforeBalance;
    private int afterBalance;

    public DTObankAction(CustomerBankAction action){
        this.yazActionTime = action.getYazActionTime();
        this.amountOfMoney = action.getAmountOfMoney();
        this.sign = action.getSign();
        this.beforeBalance = action.getBeforeBalance();
        this.afterBalance = action.getAfterBalance();
    }

    public int getAmountOfMoney() {
        return amountOfMoney;
    }

    public char getSign() {
        return sign;
    }

    public int getAfterBalance() {
        return afterBalance;
    }

    public int getBeforeBalance() {
        return beforeBalance;
    }

    public int getYazActionTime() {
        return yazActionTime;
    }
}
