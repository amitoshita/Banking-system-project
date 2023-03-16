package customers;


import loans.Loan;

import java.util.*;

public class Customer {
    private String name;
    private int balance;
    private Map<String, Loan> borrowers = new HashMap<>(); //my loans
    private Map<String, Loan> lenders = new HashMap<>(); //other loans
    private CustomerBankActionList listAction;
    private List<String> riskNotifications = new ArrayList<>();

    //constructor
    public Customer(String name, int balance){
        this.balance = balance;
        this.name = name;
        listAction = new CustomerBankActionList();
       // StatusCountBorrowers = new HashMap<>();
        //StatusCountLenders = new HashMap<>();
    }



    /*public Customer(AbsCustomer customer){
        this.balance = customer.getAbsBalance();
        this.name = customer.getName();
        listAction = new CustomerBankActionList();
       //StatusCountBorrowers = new HashMap<>();
        //StatusCountLenders = new HashMap<>();
    }*/

    public String getName(){
        return name;
    }

    public int getBalance(){
        return balance;
    }



    //add new balance
    public void addToBalance(int sum, int currYaz) {
        CustomerBankAction bankAction = new CustomerBankAction(currYaz, sum, '+', this.balance);
        listAction.getActionsList().add(bankAction);
        balance = balance + sum;
    }


    //minus new balance
    public void minusFromBalance(int sum, int currYaz){
        CustomerBankAction bankAction = new CustomerBankAction(currYaz, sum, '-', this.balance);
        listAction.getActionsList().add(bankAction);
        balance = balance - sum;
    }

    //add loan to borrowers list - my loans
    public void addLoanBorrower(Loan currLoan){
        borrowers.put(currLoan.getID(), currLoan);
    }

    //add loan to lenders list - others loans
    public void addLoanLender(Loan currLoan){
        lenders.put(currLoan.getID(), currLoan);
    }

    public void removeLoanLender(Loan currLoan){
        lenders.remove(currLoan.getID());
    }

    public CustomerBankActionList getCustomerActionsList(){
        return listAction;
    }

    public Map<String, Loan> getBorrowers() {
        return borrowers;
    }

    public Map<String, Loan> getLenders() {
        return lenders;
    }

    public void addToRiskNotifications(String str){
        this.riskNotifications.add(str);
    }

    public List<String> getRiskNotifications(){
        return this.riskNotifications;
    }
}
