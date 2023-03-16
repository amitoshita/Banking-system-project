package loans;

import java.util.HashSet;
import java.util.Set;

public class LoanCategories {
    private Set<String> allCategories;
    public LoanCategories(){
        this.allCategories = new HashSet<>();
    }
    public void addCategory(String name){
        if(!allCategories.contains(name)){
            allCategories.add(name);
        }
    }
    public Set<String> getAllCategories(){
        return allCategories;
    }

}

