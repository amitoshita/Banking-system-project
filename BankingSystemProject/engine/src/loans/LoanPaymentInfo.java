package loans;

import general.GeneralYazTime;

public class LoanPaymentInfo {

    private int fund; //KEREN
    private int interest; //RIBIT
    private int loanPayment; //fund + interest (RIBIT + KEREN)
    private int StartYaz;
    private int restPayToCustomer;
    private int interestPrecent;
    private int lastPayment;
    private boolean ifForSell;
    private int fundRest;

    public LoanPaymentInfo(int loanAmount, int interestPrecent){
        this.fund = loanAmount;
        this.interest= ((interestPrecent * loanAmount) / 100);
        this.interestPrecent = interestPrecent;
        this.loanPayment = (this.interest + this.fund);
        this.StartYaz = GeneralYazTime.getCurrYaz(); //LoanYaz.getCurrYazTime();
        this.restPayToCustomer = loanPayment;
        this.ifForSell = false;
        this.fundRest = fund;
    }

    public int getFundRest() {
        return fundRest;
    }

    public void addYazTime(int yazToAdd){
        lastPayment += yazToAdd;
    }

    public int getStartYaz(){
        return StartYaz;
    }

    public int getFund() {
        return fund;
    }

    public void addMoneyToSameCustomer(int amount, int interestPrecent){
        fund += amount;
        interest= (interestPrecent/100) * fund;
        loanPayment = this.interest + fund;
    }

    public int getInterest() {
        return interest;
    }

    public int getLoanPayment() {
        return loanPayment;
    }

    public void minusRestPay (int pay)
    {
        this.restPayToCustomer = this.restPayToCustomer-pay;
        int temp = ((pay*interest)/100);
        this.fundRest -= temp;
    }

    public int getRestPayToCustomer (){
        return this.restPayToCustomer;
    }

    public void setIfForSell(boolean b) {
        this.ifForSell = b;
    }

    public boolean isIfForSell() {
        return ifForSell;
    }
}

