package customers;

import java.util.LinkedList;
import java.util.List;

public class CustomerBankActionList {
    List<CustomerBankAction> actions;

    public CustomerBankActionList(){
        actions = new LinkedList<>();
    }

    public List<CustomerBankAction> getActionsList(){
        return actions;
    }

   /* public int getYazActionByIdLoan(String ID)
    {
        CustomerBankAction a =
        actions.stream()
                .filter(action -> ID.equals(action.getLoanID()))
                .findAny()
                .orElse(null);
        return a.getYazActionTime();
    }*/
}
