package dtos;

import customers.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOallCustomers implements DTO {

    private List<DTOcustomer> allCustomers;
    private boolean isUserExist = false;

    public DTOallCustomers(Map<String, Customer> tempAllCustomers){
        allCustomers = new ArrayList<>();
        for(Customer l: tempAllCustomers.values()){
            DTOcustomer currCustomer = new DTOcustomer(l);
            allCustomers.add(currCustomer);
        }

    }

    public List<DTOcustomer> getAllCustomers() {
        return allCustomers;
    }

}
