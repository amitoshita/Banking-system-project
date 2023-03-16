package general;

import customers.Customer;
import customers.CustomersList;
import jaxbClientEX3.AbsDescriptor;
import jaxbClientEX3.AbsLoan;
import loans.Loan;
import loans.LoansList;
import loans.status.StatusENUM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Descriptor {
    private LoansList loansList;
    private GeneralYazTime currYaz;
    private Transport transport;
    private Map<Integer, LoansList> loansListState;

    public Descriptor(Transport t){
        loansList = new LoansList();
        currYaz = new GeneralYazTime();
        loansListState = new HashMap<>();
        transport = t;
    }

    public void saveCurrStateLoans(){
        LoansList tempLoan = new LoansList();
        for(Loan l : loansList.getAllLoans().values()){
            tempLoan.addNode(l);
        }
        loansListState.put(GeneralYazTime.getCurrYaz(), tempLoan);
    }

    public Map<Integer, LoansList> getLoansListState() {
        return loansListState;
    }
    /*public Descriptor(AbsDescriptor tempDescriptor, String name){
       List<AbsLoan> tempLoanList = tempDescriptor.getAbsLoans().getAbsLoan();
       for(AbsLoan l : (tempLoanList)) {
           Loan loan = new Loan(l, name);
           loansList.addNode(loan);
           customersList.getAllCostumers().get(name).addLoanBorrower(loan);
       }

        /*List<AbsCustomer> tempCustomerList = tempDescriptor.getAbsCustomers().getAbsCustomer();
        for(AbsCustomer c : tempCustomerList) {
            Customer customer = new Customer(c);
            //customersList.addCustomer(customer);
        }*/

       /* for (Map.Entry<String, Loan> l : loansList.getAllLoans().entrySet()){
            for(Customer c : customersList.getAllCostumers().values()){
                if(c.getName().equals(l.getValue().getLoanOwner())) {
                    c.addLoanBorrower(l.getValue());
                }
            }
        }*/

    public void createLoanUserList(AbsDescriptor tempDescriptor, String name){
        List<AbsLoan> tempLoanList = tempDescriptor.getAbsLoans().getAbsLoan();
        for(AbsLoan l : (tempLoanList)) {
        Loan loan = new Loan(l, name);
        loansList.addNode(loan);
        transport.addCategory(loan.getLoanCategory());
        transport.getCustomersList().getAllCostumers().get(name).addLoanBorrower(loan);
        }
    }

    public void createLoan(String name, String loanID, String category, int amount, int totalYaz, int payEveryYaz, int interest){

        Loan newLoan = new Loan(loanID, name, category, amount, totalYaz, payEveryYaz, interest, StatusENUM.PENDING);
        loansList.addNode(newLoan);
        transport.addCategory(category);
        transport.getCustomersList().getAllCostumers().get(name).addLoanBorrower(newLoan);
    }

    public Map<String, Loan> getLoanList()
    {
        return this.loansList.getAllLoans();
    }

    public int getCurrYaz() {
        return currYaz.getCurrYaz();
    }


}



