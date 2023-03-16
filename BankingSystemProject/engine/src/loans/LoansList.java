package loans;

import java.util.HashMap;
import java.util.Map;

public class LoansList {
    private Map<String, Loan> allLoans;

    /*returns all the loans list*/
    public Map<String, Loan> getAllLoans() {
        if (allLoans == null) {
            allLoans = new HashMap<>();
        }
        return this.allLoans;

    }

    public void addNode(Loan loan){
        if(this.allLoans == null)
        {
            allLoans = new HashMap<String, Loan>();
        }
        allLoans.put(loan.getID(), loan);
    }


}
