package customers;

public class CustomerBankAction {

    private int yazActionTime;
    private int amountOfMoney;
    private char sign;
    private int beforeBalance;
    private int afterBalance = 0;
    //private String loanID; // should we use it ???

    public CustomerBankAction(int yaz, int amount, char sign, int beforeBalance){
        this.yazActionTime=yaz;
        this.amountOfMoney=amount;
        this.sign=sign;
        this.beforeBalance=beforeBalance;
        afterBalance();
        //this.loanID=loanID;
    }

    public int getYazActionTime(){
       return yazActionTime;
    }

    public int getBeforeBalance() {
        return beforeBalance;
    }

    public int getAfterBalance() {
        return afterBalance;
    }

    public char getSign() {
        return sign;
    }

    public int getAmountOfMoney() {
        return amountOfMoney;
    }

    public void afterBalance(){
        if(sign == '-'){
            afterBalance = beforeBalance - amountOfMoney;
        }
        if(sign == '+'){
            afterBalance = beforeBalance + amountOfMoney;
        }
    }

    @Override
    public String toString() {
        String str = "";
        str += "The Action yaz time is: " + yazActionTime + "\n" +
                "The amount of money he got or give is: " + sign + amountOfMoney + "\n" +
                "The amount of money before the action is: " + beforeBalance + "\n" +
                "The amount of money after the action is: " + afterBalance;
        return str;
    }
}
