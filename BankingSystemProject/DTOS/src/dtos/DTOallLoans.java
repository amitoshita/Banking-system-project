package dtos;

import loans.Loan;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOallLoans implements DTO {

    private List<DTOLoan> allLoans = new ArrayList<>();

    public DTOallLoans(Map<String, Loan> tempAllLoans){
        //allLoans = new ArrayList<>();
        for(Loan l: tempAllLoans.values()){
            DTOLoan currLoan = new DTOLoan(l);
            allLoans.add(currLoan);
        }
    }

    public List<DTOLoan> getDTOloanList(){
        return allLoans;
    }

}
