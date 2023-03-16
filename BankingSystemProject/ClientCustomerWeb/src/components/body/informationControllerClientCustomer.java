package components.body;

import dtos.DTOLoan;
import dtos.DTOallBankActions;
import dtos.DTObankAction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;
import java.util.Optional;

import static components.app.appControllerClientCustomer.getError;

public class informationControllerClientCustomer {

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
    private BodyControllerClientCustomer customerTabController = null;
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
    public void setMainController(BodyControllerClientCustomer customerTabController){
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
                sendMoneyUpdateToServer(result.get(), "-");
            }
            else{
                getErrorOfLessMoney();
            }
            /*customerTabController.updateAdminTablesToAppController();*/
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
            sendMoneyUpdateToServer(result.get(), "+");
        }
        /*customerTabController.updateAdminTablesToAppController();*/
    }

    public void sendMoneyUpdateToServer(String moneyToConvey, String sign){
        String finalUrl = HttpUrl
                .parse(Constants.UPDATE_MONEY)
                .newBuilder()
                .addQueryParameter("money", moneyToConvey)
                .addQueryParameter("action", sign)
                .build()
                .toString();

        HttpClientUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("Unable to charge money.")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            getError(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            customerTabController.setInformationTab();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void getErrorOfLessMoney()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText("You don't have enough money in your bank account");
        errorAlert.showAndWait();
    }

    public void changeToDarkMode(){
        scrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        scrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }


    public void changeIfRewind(Boolean bool){
        LoanerLoansTable.setEditable(!bool);
        ChargeButton.setDisable(bool);
        WithDrawButton.setDisable(bool);
    }
}
