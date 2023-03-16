package dtos;

import customers.CustomerBankAction;
import java.util.ArrayList;
import java.util.List;

public class DTOallBankActions {

    private List<DTObankAction> allBankActions;

    public DTOallBankActions(List<CustomerBankAction> allActions){
        allBankActions = new ArrayList<>();
        for(CustomerBankAction l: allActions){
            DTObankAction currAction = new DTObankAction(l);
            allBankActions.add(currAction);
        }
    }

    public List<DTObankAction> getDTOActionsList(){
        return allBankActions;
    }

}
