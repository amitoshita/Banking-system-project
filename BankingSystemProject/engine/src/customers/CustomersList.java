package customers;

import loans.Loan;

import java.util.*;

public class CustomersList {

    private Map<String, Customer> allCostumers;

    public CustomersList(){
        allCostumers = new HashMap<>();
    }

    public Map<String, Customer> getAllCostumers() {
        return this.allCostumers;
    }

    public synchronized void addCustomer(String customer){
        allCostumers.put(customer, new Customer(customer, 0));
    }

    public synchronized void removeUser(String username) {
        allCostumers.remove(username);
    }

    public synchronized Map<String, Customer> getUsers() {
        return allCostumers;
    }

    public boolean isUserExists(String username) {
        return allCostumers.containsKey(username);
    }
}
