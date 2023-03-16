package dtos;

import customers.Customer;

import java.util.ArrayList;
import java.util.List;

public class DTOcustomer {

    private String name;
    private DTOallLoans borrowers; //my loans
    private DTOallLoans lenders; //other loans
    private DTOallBankActions listActions;
    private int balance;
    private List<String> riskNotifications = new ArrayList<>();

    public DTOcustomer(Customer customer){
        this.name = customer.getName();
        this.balance = customer.getBalance();
        this.borrowers = new DTOallLoans(customer.getBorrowers());
        this.lenders = new DTOallLoans(customer.getLenders());
        this.listActions = new DTOallBankActions(customer.getCustomerActionsList().getActionsList());
        this.riskNotifications = customer.getRiskNotifications();
        //StatusCountBorrowers = new HashMap<>();
       // StatusCountLenders = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public DTOallBankActions getListActions() {
        return listActions;
    }

    public DTOallLoans getLenders() {
        return lenders;
    }

    public DTOallLoans getBorrowers() {
        return borrowers;
    }

    public int getBalance() {
        return balance;
    }

    public int customerOpenLoans(){
        int count = 0;
        for(DTOLoan l : borrowers.getDTOloanList()) {

            if(l.getLoanStatus().getName() != "finished")
            {
                count++;
            }
        }
        return count;
    }

    public List<String> getRiskNotifications(){
        return riskNotifications;
    }


}
