package body.customer;

import dtos.DTOLoan;
import dtos.DTOallBankActions;
import dtos.DTObankAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;

public class InformationCustomerController {

    @FXML private ScrollPane customerLoanBorrower;
    @FXML private TableView<DTOLoan> LoanerLoansTable;
    @FXML private TableColumn<DTOLoan, String> LoanIdLoaner;
    @FXML private TableColumn<DTOLoan, Integer> AmountLoaner;
    @FXML private TableColumn<DTOLoan, String> StatusLoaner;
    @FXML private TableColumn<DTOLoan, String> CategoryLoaner;
    @FXML private TableColumn<DTOLoan, Integer> InterestLoaner;
    @FXML private TableColumn<DTOLoan, Integer> StartYazLoaner;
    @FXML private TableColumn<DTOLoan, Integer> EndingYazLoaner;
    @FXML private TableColumn<DTOLoan, Integer> TotalYazLoaner;
    @FXML private TableColumn<DTOLoan, Integer> PayEveryLoaner;
    @FXML private TableColumn<DTOLoan, Integer> MoneyRaisedLoaner;
    @FXML private TableColumn<DTOLoan, Integer> MoneyLeftLoaner;
    @FXML private ScrollPane customerLoanLenders;
    @FXML private TableView<DTOLoan> LenderLoansTable;
    @FXML private TableColumn<DTOLoan, String> LoanIdLender;
    @FXML private TableColumn<DTOLoan, Integer> AmountLender;
    @FXML private TableColumn<DTOLoan, String> OwnerLender;
    @FXML private TableColumn<DTOLoan, String> StatusLender;
    @FXML private TableColumn<DTOLoan, String> CategoryLender;
    @FXML private TableColumn<DTOLoan, Integer> InterestLender;
    @FXML private TableColumn<DTOLoan, Integer> StartYazLender;
    @FXML private TableColumn<DTOLoan, Integer> EndingYazLender;
    @FXML private TableColumn<DTOLoan, Integer> TotalYazLender;
    @FXML private TableColumn<DTOLoan, Integer> PayEveryYazLender;
    @FXML private ScrollPane customerBankActions;
    @FXML private Button ChargeButton;
    @FXML private Button WithDrawButton;
    @FXML private Label moneyAmountLabel;
    @FXML private TableColumn<DTObankAction, Integer> BankActionsYaz;
    @FXML private TableColumn<DTObankAction, Integer> BankActionsAmount;
    @FXML private TableColumn<DTObankAction, String> BankActionsSign;
    @FXML private TableView<DTObankAction> bankActionsTable;
    @FXML private ScrollPane scrollPane;
    private CustomerTabController customerTabController = null;
    private ObservableList<DTObankAction> BankActionList;
    private int customerBalance;



    @FXML
    public void initialize(){

        LoanIdLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("IdLoan"));
        AmountLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("loanAmount"));
        StatusLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("loanStatus"));
        CategoryLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("category"));
        InterestLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("interest"));
        StartYazLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("startActiveYaz"));
        EndingYazLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("endingYaz"));
        TotalYazLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalYazTime"));
        PayEveryLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("paysEveryYaz"));
        MoneyRaisedLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalFund"));
        MoneyLeftLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("fundLeft"));
        LoanIdLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("IdLoan"));
        AmountLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("loanAmount"));
        OwnerLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("loanOwner"));
        StatusLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("loanStatus"));
        CategoryLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("category"));
        InterestLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("interest"));
        StartYazLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("startActiveYaz"));
        EndingYazLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("endingYaz"));
        TotalYazLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalYazTime"));
        PayEveryYazLender.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("paysEveryYaz"));
        BankActionsYaz.setCellValueFactory(new PropertyValueFactory<DTObankAction, Integer>("yazActionTime"));
        BankActionsAmount.setCellValueFactory(new PropertyValueFactory<DTObankAction, Integer>("amountOfMoney"));
        BankActionsSign.setCellValueFactory(new PropertyValueFactory<DTObankAction, String>("sign"));


    }
    public void setMainController(CustomerTabController customerTabController){
        this.customerTabController = customerTabController;
    }

    public void setLendersLoanList(ObservableList<DTOLoan> lst) throws IOException {
        ObservableList<DTOLoan> LendersList = FXCollections.observableList(lst);
        LenderLoansTable.setItems(LendersList);
    }

    public void setLoanersLoanList(ObservableList<DTOLoan> lst) throws IOException {
        ObservableList<DTOLoan> LoanersList = FXCollections.observableList(lst);
        LoanerLoansTable.setItems(LoanersList);
    }

    public void setCustomerBalance(int balance){
        this.customerBalance = balance;
        moneyAmountLabel.setText("Your money amount is: " + this.customerBalance + "$");
    }

    public void setCustomerBankActions(DTOallBankActions bankActions){

        BankActionList = FXCollections.observableList(bankActions.getDTOActionsList());
        bankActionsTable.setItems(BankActionList);
    }

    @FXML
    void WithdrawPopUpWindow(ActionEvent event) throws IOException {
        TextInputDialog dialogWithdraw = new TextInputDialog();

        dialogWithdraw.setTitle("Withdraw");
        dialogWithdraw.setHeaderText("Enter amount of money to withdraw:");
        dialogWithdraw.setContentText("Amount:");
        Optional<String> result = dialogWithdraw.showAndWait();
        //To set exception if the input not valid

        if (result.isPresent()){
            if (this.customerBalance >= Integer.parseInt(result.get())) {
                this.customerTabController.minusMoneyToCustomer(result.get());
                this.customerTabController.setInformationTab();
            }
            else{
                getErrorOfLessMoney();
            }
            customerTabController.updateAdminTablesToAppController();
        }
    }


    @FXML
    void chargePopUpWindow(ActionEvent event) throws IOException {
        TextInputDialog dialogCharge = new TextInputDialog();

        dialogCharge.setTitle("Charge");
        dialogCharge.setHeaderText("Enter amount of money to charge:");
        dialogCharge.setContentText("Amount:");
        Optional<String> result = dialogCharge.showAndWait();
        //To set exception if the input not valid

        if (result.isPresent()){
            this.customerTabController.addMoneyToCustomer(result.get());
            this.customerTabController.setInformationTab();
        }
        customerTabController.updateAdminTablesToAppController();
    }

    public void getErrorOfLessMoney()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("You don't have enough money in your bank account");
        errorAlert.showAndWait();
    }

    public void changeToDarkMode(){
        scrollPane.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        scrollPane.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
    }

}
