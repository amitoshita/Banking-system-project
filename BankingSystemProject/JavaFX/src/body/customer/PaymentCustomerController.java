package body.customer;

import dtos.DTOLoan;
import dtos.DTObankAction;
import dtos.DTOcustomer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentCustomerController {

    private CustomerTabController customerTabController = null;
    @FXML private ScrollPane customerBankActions;
    @FXML private ListView<String> NotificationArea = new ListView<>();
    @FXML private TableView<DTOLoan> LoanerLoansTable;
    @FXML private TableColumn<DTOLoan, String> LoanIdLoaner;
    @FXML private TableColumn<DTOLoan, String> StatusLoaner;
    @FXML private TableColumn<DTOLoan, Integer> TotalYazLoaner;
    @FXML private TableColumn<DTOLoan, Integer> PayEveryLoaner;
    @FXML private TableColumn<DTOLoan, Integer> amountEveryPulseLoaner;
    @FXML private TableColumn<DTOLoan, Integer> totalAmountLoaner;
    @FXML private TableColumn<DTOLoan, Boolean> checkBoxChoice;
    @FXML private TableColumn<DTOLoan, Integer> debtID;
    @FXML private ScrollPane Scroll;
    @FXML private TextField amountPayText;
    @FXML private Button ButtonPayAll;
    @FXML private Button ButtonPayPulse;
    @FXML private Button ConveyMoneyOnRiskButton;
    private ObservableList<DTOLoan> LendersList;
    private List<DTOLoan> checkedPaymentLoans = new ArrayList<>();


    @FXML
    public void initialize() {
        LoanerLoansTable.setEditable(true);
        debtID.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("debt"));
        LoanIdLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("IdLoan"));
        StatusLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("loanStatus"));
        TotalYazLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalYazTime"));
        PayEveryLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("paysEveryYaz"));
        amountEveryPulseLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("amountEveryPulse"));
        totalAmountLoaner.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("fundLeft"));
        checkBoxChoice.setCellFactory(col -> {
            CheckBoxTableCell<DTOLoan, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty checked = new SimpleBooleanProperty(LoanerLoansTable.getItems().get(index).getCheckProperty());
                checked.addListener((obs, NotSelected, selected) -> {
                    DTOLoan item = LoanerLoansTable.getItems().get(index);
                    item.setSelected(selected);
                    if (selected) {
                        if (item.getLoanStatus().getName().equals("risk")) {
                            amountPayText.setDisable(false);
                            ConveyMoneyOnRiskButton.setDisable(false);
                            this.ButtonPayAll.setDisable(true);
                            this.ButtonPayPulse.setDisable(true);
                        }
                        else {
                            this.ButtonPayAll.setDisable(false);
                            this.ButtonPayPulse.setDisable(false);
                            amountPayText.setDisable(true);
                            ConveyMoneyOnRiskButton.setDisable(true);
                        }
                        LoanerLoansTable.setEditable(false);
                    } else if (!selected) {
                        amountPayText.setDisable(true);
                        ConveyMoneyOnRiskButton.setDisable(true);
                        this.ButtonPayAll.setDisable(true);
                        this.ButtonPayPulse.setDisable(true);
                    }
                });
                return checked;
            });

            return cell;
        });

    }

    public void setMainController(CustomerTabController customerTabController) {
        this.customerTabController = customerTabController;
    }

    @FXML
    void clickPayAll(ActionEvent event) throws IOException {
        createChosenLoanList();
        if (!checkedPaymentLoans.isEmpty()) {
            boolean check = customerTabController.conveyAllMoneyToLenders(checkedPaymentLoans.get(0));
            customerTabController.setInformationTab();
            customerTabController.setPaymentTab();
            customerTabController.updateAdminTablesToAppController();
            checkedPaymentLoans = new ArrayList<>();
            if (check == false) {
                getErrorException("You do not have enough money in your account");
            } else {
                updateNotificationOnPaymentTab();
                getDoneMessage();
            }
            customerTabController.updateAdminTablesToAppController();
        } else {
            getErrorException("Please choose one loan!");
        }
    }

    @FXML
    void clickPayOnePulse(ActionEvent event) throws IOException {
        createChosenLoanList();

        if (!checkedPaymentLoans.isEmpty()) {
            boolean check = customerTabController.conveyMoneyToLenders(checkedPaymentLoans.get(0));
            customerTabController.setInformationTab();
            customerTabController.setPaymentTab();
            checkedPaymentLoans = new ArrayList<>();
            if (check == false) {
                getErrorException("You do not have enough money in your account");
            } else {
                updateNotificationOnPaymentTab();
                getDoneMessage();
            }
            customerTabController.updateAdminTablesToAppController();
        } else {
            getErrorException("Please choose one loan!");
        }
    }

    public List<DTOLoan> createChosenLoanList() {
        for (DTOLoan l : LendersList) {
            if (l.getCheckProperty()) {
                checkedPaymentLoans.add(l);
                l.setSelected(false);
            }
        }
        LoanerLoansTable.setEditable(true);
        return checkedPaymentLoans;
    }

    public void initLoansCheck(){
        for (DTOLoan l : LendersList) {
            if (l.getSelected() == true) {
                l.setSelected(false);
            }
        }
    }


    @FXML
    void ConveyMoneyOnRisk(ActionEvent event) throws IOException {
        createChosenLoanList();

        if (!checkedPaymentLoans.isEmpty()) {
            boolean convey = customerTabController.conveyMoneyToRiskLoanCustomerTab(Integer.parseInt(amountPayText.getText()), checkedPaymentLoans.get(0));
            customerTabController.setInformationTab();
            customerTabController.setPaymentTab();
            checkedPaymentLoans = new ArrayList<>();
            if (convey == false) {
                getErrorException("You do not have enough money in your account");
            } else {
                getDoneMessage();
            }
            customerTabController.updateAdminTablesToAppController();
        } else {
            getErrorException("Please choose one loan!");
        }
    }


    public void setLendersActiveRiskLoanList(ObservableList<DTOLoan> lst) throws IOException {
        LendersList = FXCollections.observableList(lst);
        LoanerLoansTable.setItems(LendersList);
    }

    public void getErrorException(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public void getDoneMessage() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("Done!");
        errorAlert.setContentText("The process was performed successfully");
        errorAlert.showAndWait();
    }

    public void changeToDarkMode() {
        Scroll.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        Scroll.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        Scroll.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        Scroll.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
    }

    @FXML
    void clickClearButton(ActionEvent event) throws IOException {
        initLoansCheck();
        customerTabController.setPaymentTab();
        LoanerLoansTable.setEditable(true);
    }


    public void updateNotificationOnPaymentTab(){
        DTOcustomer c = customerTabController.getCustomerByNameCustomerTab();
        NotificationArea.getItems().setAll(c.getRiskNotifications());
    }


}
