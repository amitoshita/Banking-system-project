package main;

import dtos.*;
import exceptions.fileCategoriesException;
import exceptions.fileCustomersNamesException;
import exceptions.fileDivideYazPaymentException;
import exceptions.fileExtentionException;
import general.Transport;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;

public class UImain {
    public static void main(String[] args) {
        /* to show board to user */
        /* need to give a xml path */
        /* while loop - menu */
        Transport transport = new Transport();
        int choose;
        do {
            Scanner scanner = new Scanner(System.in);
            PrintUserMenu();
            try{
            choose = scanner.nextInt();
            RunTheGame(choose , transport);
            }catch (InputMismatchException e){
                System.out.println("You entered a wrong number! please try number between 1-8 \n");
                choose = 0;
            }
        } while(choose != 8);


        System.out.println("BYE");
    }


    public static void PrintUserMenu() {
        System.out.println("\nWelcome to banking system. choose action to do:" + "\n" +
                "1. Import XML file." + "\n" +
                "2. Show the information about all the loans." + "\n" +
                "3. Show the information about all customers." + "\n" +
                "4. Charge money to the account." + "\n" +
                "5. Take money out of the account." + "\n" +
                "6. To loan money to another customer. " + "\n" +
                "7. Promoting the timeline and giving the payments." + "\n" +
                "8. Exit" + "\n");
    }

    public static void RunTheGame(int choose, Transport transport) {
        switch (choose){
            case 1:
                System.out.println("Please enter an xml file: \n");
                loadFileAndCheckExceptions(transport);
                break;
            case 2:
                if(transport.getIfFileIsGood()==true) {
                    DTOallLoans dtoAllLoans = transport.getDTOLoans();
                    printAllLoans(dtoAllLoans.getDTOloanList());
                }
                else {
                    System.out.println("XML file does not exist, \n" +
                            "please choose option (1) in the menu and try again! \n ");

                }
                break;

            case 3:
                if(transport.getIfFileIsGood()==true) {
                    DTOallCustomers dtoAllCustomers = transport.getDTOCustomers();
                    printAllCustomers(dtoAllCustomers);
                }
                else {
                    System.out.println("XML file does not exist, \n" +
                            "please choose option (1) in the menu and try again! \n ");

                }
                break;

            case 4:
                if(transport.getIfFileIsGood()==true) {
                    DTOallCustomers dtoAllCustomersNames1 = transport.getDTOCustomers();
                    printAllCostumersNames(dtoAllCustomersNames1);
                    AddMoneyByCostumer(transport);
                }
                else {
                    System.out.println("XML file does not exist, \n" +
                            "please choose option (1) in the menu and try again! \n ");

                }
                break;

            case 5:
                if(transport.getIfFileIsGood()==true) {
                    DTOallCustomers dtoAllCustomersNames2 = transport.getDTOCustomers();
                    printAllCostumersNames(dtoAllCustomersNames2);
                    minusMoneyByCostumer(transport);
                }
                else {
                    System.out.println("XML file does not exist, \n" +
                            "please choose option (1) in the menu and try again! \n ");}
                break;
            case 6:
                if(transport.getIfFileIsGood()==true) {
                    matchLoanByCustomer(transport);
                    transport.changeLoanStatusToActiveAndCheck();
                }
                else {
                    System.out.println("XML file does not exist, \n" +
                            "please choose option (1) in the menu and try again! \n ");

                }
                break;
            case 7:
                if(transport.getIfFileIsGood()==true) {
                    System.out.println("The yaz before the Promoting the timeline is: " + transport.getLoanYaz() + "\n");
                    transport.addLoanYaz();
                    System.out.println("The yaz after the promoting is: " + transport.getLoanYaz());
                    transport.payLoanByYazTime();
                    transport.checkFinishedLoan();
                }
                else {
                    System.out.println("XML file does not exist, \n" +
                            "please choose option (1) in the menu and try again! \n ");

                }
                break;
            case 8:
                break;
            default :
                System.out.println("You entered a wrong number! please try number between 1-8 \n");
            break;

        }
    }

    public static void loadFileAndCheckExceptions(Transport transport){
        try{
            Scanner userPath = new Scanner(System.in);
            String str = userPath.nextLine();
            transport.loadNewXMLFile(str);
            System.out.println("The file was uploaded correctly!!!\n");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!" + "\n");
        }catch (fileExtentionException e) {
            System.out.println(e.getMessage());
        }catch (JAXBException e) {
            System.out.println("File is not good!" + "\n");
        }catch (fileCategoriesException e) {
            System.out.println(e.getMessage());
        }catch (fileCustomersNamesException e) {
            System.out.println(e.getMessage());
        }catch (fileDivideYazPaymentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printAllLoans(List<DTOLoan> allLoans){
        int num = 1;
        for(DTOLoan l : allLoans){
            System.out.println(num + ")");
            System.out.println("The ID of the loan is: " + l.getIdLoan() + "\n"
                    + "The loan owner is: " + l.getLoanOwner() + "\n"
                    + "The loan category is: " + l.getCategory() + "\n"
                    + "The loan amount is: " + l.getLoanAmount() + "\n"
                    + "The total yaz amount is: " + l.getTotalYazTime() + "\n"
                    + "You need to pay every yaz: " + l.getPaysEveryYaz() + "\n"
                    + "The loan interest is: " + l.getInterest() + "\n"
                    + "The loan status is: " + l.getLoanStatus().getName() + "\n" );
            printByStatus(l, true); //true = if the status is loan's
            System.out.println("_______________________________________________");
            num++;
        }
    }

    public static void printAllCustomers(DTOallCustomers allCustomers){
        int row = 1;
        for(DTOcustomer c : allCustomers.getAllCustomers()){
            System.out.println("The name of the customer is: " + c.getName() + "\n"
            + "His Bank actions are: " + "\n");
            for(DTObankAction b : c.getListActions().getDTOActionsList()){
                System.out.println(row + ") " + "The action yaz time is: " + b.getYazActionTime()
                + "\n   " + "The amount of his action is: " + b.getSign() + b.getAmountOfMoney()
                + "\n   " + "The balance before the action is: " + b.getBeforeBalance()
                + "\n   " + "The balance after the action is: " + b.getAfterBalance() + "\n");
                row ++;
            }
            row = 1;
            System.out.println("\n" + "* My loans are: " + "\n");
            for(DTOLoan l : c.getBorrowers().getDTOloanList()){
                System.out.println(row + ") " + "The loan ID is: " + l.getIdLoan() + "\n   "
                + "The fund of the loan is: " + l.getLoanAmount() + "\n   "
                + "The yaz per payment is: " + l.getPaysEveryYaz() + "\n   "
                + "The interest in percent is: " + l.getInterest() + "\n   "
                + "The total amount (fund+interest) is: " + l.getTotalAmountReturn() + "\n   "
                + "The loan status is: " + l.getLoanStatus() + "\n   ");
                printByStatus(l, false);
                row++;
            }
            row = 1;
            System.out.println("\n" + "* The loans I have invested in are: " + "\n");
            for(DTOLoan o : c.getLenders().getDTOloanList()) {
                System.out.println(row + ") " + "The loan ID is: " + o.getIdLoan() + "\n   "
                        + "The fund of the loan is: " + o.getLoanAmount() + "\n   "
                        + "The yaz per payment is: " + o.getPaysEveryYaz() + "\n   "
                        + "The interest in percent is: " + o.getInterest() + "\n   "
                        + "The total amount (fund+interest) is: " + o.getTotalAmountReturn() + "\n   "
                        + "The loan status is: " + o.getLoanStatus() + "\n   ");
                printByStatus(o, false);
                row++;
            }
            System.out.println("_______________________________________________");
        }
    }
    //to use predict (?)

    public static void printByStatus(DTOLoan loan, boolean bool){

        if (loan.getLoanStatus().getName().equals("pending")){
            if(bool == true) {
                getPendingInfoByLoan(loan);
            }
            else{
                getPendingInfoByCostumer(loan);
            }
        }
        if (loan.getLoanStatus().getName().equals("active")){
            if(bool == true) {
                getActiveInfoByLoan(loan);
            }
            else{
                getActiveInfoByCostumer(loan);
            }
        }
        if (loan.getLoanStatus().getName().equals("risk")){
            if(bool == true) {
                getActiveInfoByLoan(loan);
                getRiskInfoByLoan(loan);
            }
            else {
                getRiskInfoByLoan(loan);
            }
        }
        if (loan.getLoanStatus().getName().equals("finished")){
            if(bool == true)
            getFinishedInfoByLoan(loan);
        }
        else {
            getFinishedInfoByCostumer(loan);
        }

    }

    public static void getActiveInfoByLoan(DTOLoan loan){

        for(DTOlender len: loan.getLenders()){
            System.out.println("The lender's name is: " + len.getLenderName() + "\n" + "Starting payment time: "
                    + len.getLenderStartYaz() + "\n" + "His amount is: " + len.getLenderFund() +
                    "\n" + "The amount of the interest: " + len.getLenderInterest() +
                    "\n" + "The total amount payment to lender is: " + len.getLenderLoanPayment());
        }
    }

    public static void getRiskInfoByLoan(DTOLoan loan){
        System.out.println("The loan owner not payed " + loan.getNotPayedYazs() + " yaz, " +
                "the amount you need to pay now is " + (loan.getAmountEveryPulse()*loan.getNotPayedYazs()));
    }

    public static void getPendingInfoByLoan(DTOLoan loan){

        for(DTOlender len: loan.getLenders()){
            System.out.println("The lender's name is: " + len.getLenderName() + "  " + "His amount is: "
                    + len.getLenderFund() + "\n");
        }
        System.out.println( "\n" + "The amount of money raised: " + loan.getTotalFund() +
                "\n" + "The amount of money left: " + (loan.getLoanAmount() - loan.getTotalFund()));
    }

    public static void getFinishedInfoByLoan(DTOLoan loan){

        for(DTOlender len: loan.getLenders()) {
            System.out.println("The lender's name is: " + len.getLenderName() + "\n" + "Starting payment time: "
                    + len.getLenderStartYaz() + "\n" + "His amount is: " + len.getLenderFund() +
                    "\n" + "The amount of the interest: " + len.getLenderInterest() +
                    "\n" + "The total amount payment to lender is: " + len.getLenderLoanPayment() +
                    "\n\n");
        }

        System.out.println("Beginning Yaz: " +  loan.getStartActiveYaz() + "\n" +
                "Ending Yaz: " + loan.getEndingYaz());

    }

    public static void getPendingInfoByCostumer(DTOLoan loan){
        System.out.println("The amount of money left to be active loan: " + (loan.getLoanAmount() - loan.getTotalFund()));
    }

    public static void getActiveInfoByCostumer(DTOLoan loan){
        System.out.println("The next yaz time to pay is: " + (loan.getPaysEveryYaz()+loan.getLastPayYaz()) + "\n"
                            +"The total payment amount (fund + interest) every pulse is: " +
                             loan.getAmountEveryPulse() + "\n");
    }


    public static void getFinishedInfoByCostumer(DTOLoan loan){
        System.out.println("Beginning Yaz: " +  loan.getStartActiveYaz() + "\n" +
                "Ending Yaz: " + loan.getEndingYaz());
    }

    public static void printAllCostumersNames(DTOallCustomers allCustomers)
    {
        System.out.println("Please choose the number of the customer:");
        int num=1;
        for(DTOcustomer c : allCustomers.getAllCustomers())
        {
            System.out.println(num + ") " + c.getName() + " \n");
            num++;
        }
    }

    public static void printAllCostumersNamesWithBalance(DTOallCustomers allCustomers){

        System.out.println("Please choose the number of the customer: ");
        int num=1;
        for(DTOcustomer c : allCustomers.getAllCustomers())
        {
            System.out.println(num + ") " + c.getName() + " his money: " + c.getBalance() + " \n");
            num++;
        }

    }

    public  static void  matchLoanByCustomer(Transport transport){
        int customer, amount;
        int category = 0, interest = 0, minLoanYaz = 0;
        String customerName;
        List<DTOLoan> filteredList;
        DTOallCustomers dtoAllCustomersNames = transport.getDTOCustomers();
        printAllCostumersNamesWithBalance(dtoAllCustomersNames);
        Scanner scanner = new Scanner(System.in);

        try {
            customer = scanner.nextInt();
            try {
            while (customer > transport.getDTOCustomers().getAllCustomers().size() || customer < 1) {
                System.out.println("You entered a wrong number of person. Try again.");
                customer = scanner.nextInt();
            }
            if(transport.GetCustomerAmountByIndex(customer)==0)
            {
                System.out.println("This customer has no money in his bank account.");
                return;
            }
                System.out.println("Please enter amount of money: \n");
                amount = scanner.nextInt();
                while ((transport.GetCustomerAmountByIndex(customer) < amount) || (amount < 1)){
                    System.out.println("You entered a lower/higher amount. Try again.");
                    amount = scanner.nextInt();
                }
                System.out.println("Would you like to choose a specific loan category? \n " +
                        "Enter 1 if yes, Enter 0 if not.");
                int toChooseCategory = scanner.nextInt();
                if(toChooseCategory == 1){
                    category = printAndChooseCategories();
                }
                System.out.println("Would you like to choose a specific interest? \n " +
                        "Enter 1 if yes, Enter 0 if not.");
                int toChooseInterest = scanner.nextInt();
                if(toChooseInterest == 1){
                    System.out.println("Please enter the minimum amount of interest: ");
                    interest = scanner.nextInt();
                }
                System.out.println("Would you like to choose a minimum yaz loan time? \n " +
                        "Enter 1 if yes, Enter 0 if not.");
                int toChooseMinLoanYaz = scanner.nextInt();
                if(toChooseMinLoanYaz == 1){
                    System.out.println("Please enter the minimum loan yaz time: ");
                    minLoanYaz = scanner.nextInt();
                }
                customerName = transport.GetCustomerNameByIndex(customer);
                //filteredList = transport.filterMatchLoanByCustomer(category, interest, minLoanYaz, customerName);
                System.out.println("Choose the relevant loans for you: ");
                //printAllLoans(filteredList);
               // chooseLoansAndDivideTheMoney(transport, amount, customerName, filteredList);
            } catch (InputMismatchException e){
                System.out.println("Wrong input, this output is not number! No changes. \n");
            }

        } catch (InputMismatchException e) {
            System.out.println("Wrong input, this output is not number! No changes. \n");
        }
    }

    public static int printAndChooseCategories(){
        int res = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose the category from the categories: \n" +
        "1. Setup a business" + "\n" +
        "2. Overdraft cover" + "\n" +
        "3. Investment" + "\n" +
        "4. Happy Event" + "\n" +
        "5. Renovate" + "\n");
        res = scanner.nextInt();
        return res;
    }

    public static void AddMoneyByCostumer (Transport transport) {
        Scanner scanner = new Scanner(System.in);
        try{
        int customerNumber = getCustomerNumberFromUser(transport);
        if(customerNumber == 0)
        {
            return;
        }
        System.out.println("Please enter amount of money: \n");
        int amount = scanner.nextInt();
        //transport.addMoneyToCustomer(customerNumber, amount);
        System.out.println("Done! \n");
        }catch (InputMismatchException e){
            System.out.println("Wrong input, try again. No change.");
        }
    }
    public static int getCustomerNumberFromUser(Transport transport){
        Scanner scanner = new Scanner(System.in);
        try{
        int customerNumber = scanner.nextInt();
        while(customerNumber > transport.getDTOCustomers().getAllCustomers().size() || customerNumber < 1){
            System.out.println("You entered a wrong number of person. Try again.");
            customerNumber = scanner.nextInt();
        }
            return customerNumber;
        }catch (InputMismatchException e){
                System.out.println("Wrong input, the output is not a number! No change.");
                return 0;
        }
    }

    public static void minusMoneyByCostumer (Transport transport) {
        Scanner scanner = new Scanner(System.in);
        try{
        int customerNumber = getCustomerNumberFromUser(transport);
        if(customerNumber == 0)
        {
            return;
        }
        System.out.println("Please enter amount of money: \n");
        int amount = scanner.nextInt();
        while (amount > transport.GetCustomerAmountByIndex(customerNumber)){
            System.out.println("Wrong input. You have less money that you think! be realistic and try again...");
            amount = scanner.nextInt();
        }
        //transport.minusMoneyToCustomer(customerNumber, amount);
            System.out.println("Done! \n");
        }catch (InputMismatchException e){
            System.out.println("Wrong input, try again. No change.");
        }
    }

    public static void chooseLoansAndDivideTheMoney(Transport transport, int moneyAmount,
                                                    String customerName, List<DTOLoan> filteredLoansList){
        boolean toConvey = false;
        Scanner scanner = new Scanner(System.in);
        Set<Integer> chosenLoans = new HashSet<>();
        int loanNum , choice;
        System.out.println("Enter a number of loan: ");
        loanNum = scanner.nextInt();
        while ((loanNum > filteredLoansList.size()) || loanNum < 1){
            System.out.println("Your choice is out of range ! Please enter a number between 1 and " + filteredLoansList.size());
            loanNum = scanner.nextInt();
        }
        chosenLoans.add(loanNum);
        System.out.println("Would you like to choose more loan? Enter 1 if yes, 0 else");
        choice = scanner.nextInt();
        while (choice == 1){
            System.out.println("Enter number of loan: ");
            loanNum = scanner.nextInt();
            while ((loanNum > filteredLoansList.size()) || loanNum < 1){
                System.out.println("Your choice is out of range ! Please enter a number between 1 and " + filteredLoansList.size());
                loanNum = scanner.nextInt();
            }
            chosenLoans.add(loanNum);
            System.out.println("Would you like to choose more? Enter 1 if yes, 0 else");
            choice = scanner.nextInt();
        }
        filteredLoansList = transport.createChosenList(chosenLoans, moneyAmount, filteredLoansList);
        //toConvey = transport.divideTheLoansMoney(filteredLoansList, moneyAmount, customerName);
        if(toConvey == false){
            System.out.println("Your amount of money higher than the sum of all loans amount." +
                    "\n" + "There is no change in your bank account. \n");
        }

    }
}
