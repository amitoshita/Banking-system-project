package loans;

import customers.Customer;
import general.GeneralYazTime;
import jaxbClientEX3.AbsLoan;
import loans.status.StatusENUM;

import javax.management.Descriptor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Loan implements Comparable<Loan>{

    private String IdLoan; //To return exception if there is this ID already
    private String loanOwner; // to change to the class owner
    private String category; //To change lo loanCategories enum
    private int loanAmount; //KEREN
    private int debt = 0; // hov
    private LoanYaz loanYaz;
    private int interest; // abs interest per payment - the precent of the RIBIT
    private StatusENUM loanStatus; //to create a new class + interface loan status
    private Map<Customer, LoanPaymentInfo> lenders; //key - the name of the person, value - the amount he loan
    private int totalFund = 0; //all the money we achieve for this loan
    private int totalInterest = 0; //total RIBIT
    private int totalAmountReturn = 0; //total KEREN + RIBIT that we need to return
    private int sumOfPaymentAlreadyPaid = 0;
    private int currAmountLeftedToReturn = 0;

    //ctor
    public Loan(String ID, String owner, String Category, int LoanAmount,
                 int totalYaz, int yazPaysEveryTime, int Interest, StatusENUM LoanStatus){
        this.IdLoan = ID;
        this.loanOwner = owner;
        this.category = Category;
        this.loanAmount = LoanAmount;
        this.loanYaz = new LoanYaz(totalYaz, yazPaysEveryTime);
        this.interest = Interest;
        this.loanStatus = LoanStatus;
        lenders = new HashMap<>();

    }

    //ctor
    public Loan(AbsLoan loan, String name){
        this.IdLoan =  loan.getId();
        this.loanOwner = name;
        this.category = loan.getAbsCategory();
        this.loanAmount = loan.getAbsCapital();
        this.loanYaz = new LoanYaz(loan.getAbsTotalYazTime(), loan.getAbsPaysEveryYaz());
        this.interest = loan.getAbsIntristPerPayment();
        this.loanStatus = StatusENUM.PENDING;
        lenders = new HashMap<>();
    }

    public String getID(){
        return this.IdLoan;
    }

    public String getLoanOwner(){
        return this.loanOwner;
    }

    public int getTotalFund() {
        return totalFund;
    }

    public int getTotalAmountReturn() {
        return totalAmountReturn;
    }

    public int getTotalInterest() {
        return totalInterest;
    }

   public String getLoanCategory(){
        return this.category;
   }

    public int getLoanAmount(){
        return this.loanAmount;
    }

    public StatusENUM getLoanStatus(){
        return this.loanStatus;
    }

    public void setLoanStatus(StatusENUM NewStatus){
        this.loanStatus = NewStatus;
    }

    //returns the map of all lenders
    public Map<Customer, LoanPaymentInfo> getLendersMap (){
        return this.lenders;
    }

    public void sumOfAmountOfInterest(int interest){
        this.totalInterest += interest;
    }

    public void sumOfAmountOfFund(int fund){
        this.totalFund += fund;
    }

    public void addLender(Customer customer, LoanPaymentInfo sum){
        lenders.put(customer, sum);
    }

     public Map<Customer, LoanPaymentInfo> getLenders (){
        return lenders;
    }

    public int getLoanInterest(){
        return this.interest;
    }

    public int fundLeft(){
        return (this.loanAmount - this.totalFund);
    }
    public LoanYaz getLoanYaz(){
        return this.loanYaz;
    }

    public int getTotalAmountToPayOnce()
    {
        return (this.totalAmountReturn/this.loanYaz.getPaymentPulseYaz());
    }

    public void updateTotalFundAndInterest () {
        int newFund = 0;
        for (LoanPaymentInfo p : lenders.values()) {
            newFund += p.getFund();
        }
        totalFund = newFund;
        totalInterest = (interest * totalFund) / 100;
        totalAmountReturn = totalFund + totalInterest;
        if (currAmountLeftedToReturn == 0) {
            currAmountLeftedToReturn = totalAmountReturn;
        }
    }
    public void MinusCurrAmountLeftedToReturn(int amount){
        currAmountLeftedToReturn -= amount;
    }
    public int getSumOfPaymentAlreadyPaid() {
        return sumOfPaymentAlreadyPaid;
    }

    @Override
    public int compareTo(Loan o) {
        if(loanYaz.getStartActiveYaz() == o.loanYaz.getStartActiveYaz())
            return 0;
        else if(loanYaz.getStartActiveYaz() > o.loanYaz.getStartActiveYaz())
            return 1;
        else
            return -1;
    }

    public void payEveryLender(int leftPulseToPay){

        int sumToConvey;
        for(Map.Entry<Customer, LoanPaymentInfo> i: lenders.entrySet()){
            sumToConvey = ((i.getValue().getLoanPayment() / (this.loanYaz.getPaymentPulseYaz())*leftPulseToPay));
            i.getValue().minusRestPay(sumToConvey);
            i.getKey().addToBalance(sumToConvey, GeneralYazTime.getCurrYaz());
            sumOfPaymentAlreadyPaid += sumToConvey;
        }
    }

    public int getRestToPayToCustomerByLenderName(Customer customer){

        return lenders.get(customer).getRestPayToCustomer();
    }

    public int payEveryLenderOnRisk(int amount) {
        int sumToConvey;
        int partToConveyByPrecentage = 0;
        if (amount <= this.debt) {
            sumToConvey = amount;
        } else {
            sumToConvey = debt;
        }
        for (Map.Entry<Customer, LoanPaymentInfo> i : lenders.entrySet()) {

            partToConveyByPrecentage = ((((i.getValue().getLoanPayment() * 100) / this.totalAmountReturn) * sumToConvey) / 100);
            i.getValue().minusRestPay(partToConveyByPrecentage);
            i.getKey().addToBalance(partToConveyByPrecentage, GeneralYazTime.getCurrYaz());
            sumOfPaymentAlreadyPaid += partToConveyByPrecentage;
            partToConveyByPrecentage = 0;
        }
        return sumToConvey;
    }

    public void payAllRestMoneyEveryLender(){
        int sumToConvey;
        for(Map.Entry<Customer, LoanPaymentInfo> i: lenders.entrySet()){
            sumToConvey = (i.getValue().getRestPayToCustomer());
            i.getValue().minusRestPay(sumToConvey);
            i.getKey().addToBalance(sumToConvey, GeneralYazTime.getCurrYaz());
            sumOfPaymentAlreadyPaid += sumToConvey;
        }
    }

    public void updateLeftPulseToPay ()
    {
        loanYaz.setNotPayPulseYaz(((GeneralYazTime.getCurrYaz() - loanYaz.getLastPayYaz()) / loanYaz.getPaysEveryYaz()));
    }

    public int leftedMoneyToPay(){
        return(totalAmountReturn-sumOfPaymentAlreadyPaid);
    }

    public Map<Integer, Boolean> getPayOrNotMap(){
        return loanYaz.getPayedOrNot();
    }

    public int getDebt() {
        return debt;
    }

    public void plusToDebt(int debt) {
        this.debt += debt;
    }

    public void minusFromDebt(int debt){
        this.debt -= debt;
    }

    public void setLenderLoanPartForSale(Customer customer, boolean bool){
        lenders.get(customer).setIfForSell(bool);
    }

    public void switchBuyerAndSellerAfterLoanBuy(Customer buyer, Customer seller, int currYaz){
        seller.removeLoanLender(this);
        buyer.addLoanLender(this);
        LoanPaymentInfo obj = lenders.remove(seller);
        lenders.put(buyer, obj);
        // להחליף את סכום הקנייה- להוריד את הריבית
        buyer.minusFromBalance(obj.getRestPayToCustomer(), currYaz);
        seller.addToBalance(obj.getRestPayToCustomer(), currYaz);
    }
}
