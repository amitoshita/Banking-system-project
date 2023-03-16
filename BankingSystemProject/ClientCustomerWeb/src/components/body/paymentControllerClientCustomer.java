package components.body;

import dtos.DTOLoan;
import dtos.DTOcustomer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static components.app.appControllerClientCustomer.getError;
import static utils.Constants.*;
import static utils.Constants.REFRESH_RATE;

public class paymentControllerClientCustomer {


    private BodyControllerClientCustomer customerTabController = null;
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
    @FXML private Button refreshButtton;
    @FXML private Button clearButton;
    private ObservableList<DTOLoan> LendersList;
    private List<DTOLoan> checkedPaymentLoans = new ArrayList<>();
    private CustomerRefresher customerRefresher;
    private Timer timer;


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

    public void setMainController(BodyControllerClientCustomer customerTabController) {
        this.customerTabController = customerTabController;
    }

    @FXML
    void clickPayAll(ActionEvent event) throws IOException {

        createChosenLoanList();
        if (!checkedPaymentLoans.isEmpty()) {
            String chosenLoan = GSON_INSTANCE.toJson(checkedPaymentLoans.get(0));

            String conveyMoney = HttpUrl
                    .parse(Constants.CONVEYMONEY)
                    .newBuilder()
                    .addQueryParameter("chosen-loan", chosenLoan)
                    .addQueryParameter("one-or-all", "all")
                    .build()
                    .toString();
            sendConveyMoneyToServer(conveyMoney);
        } else {
            getErrorException("Please choose one loan!");
        }
    }

    public void sendConveyMoneyToServer(String conveyMoney){
        HttpClientUtils.runAsync(conveyMoney, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("FAILED to pay money in one pulse.")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            getErrorException("Unable to convey the money to lenders...")

                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            customerTabController.setInformationTab();
                            customerTabController.setPaymentTab();
                            checkedPaymentLoans = new ArrayList<>();
                            getDoneMessage();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @FXML
    void clickPayOnePulse(ActionEvent event) throws IOException {
        createChosenLoanList();
        if (!checkedPaymentLoans.isEmpty()) {
            String chosenLoan = GSON_INSTANCE.toJson(checkedPaymentLoans.get(0));

            String conveyMoney = HttpUrl
                    .parse(Constants.CONVEYMONEY)
                    .newBuilder()
                    .addQueryParameter("chosen-loan", chosenLoan)
                    .addQueryParameter("one-or-all", "one")
                    .build()
                    .toString();

            sendConveyMoneyToServer(conveyMoney);
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

       /* if (!checkedPaymentLoans.isEmpty()) {
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
        }*/
    }


    public void setLendersActiveRiskLoanList(ObservableList<DTOLoan> lst) throws IOException {
        LendersList = FXCollections.observableList(lst);
        LoanerLoansTable.setItems(LendersList);
    }

    public ObservableList<DTOLoan> getLendersList(){
        return LendersList;
    }

    public static void getErrorException(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public static void getDoneMessage() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("Done!");
        errorAlert.setContentText("The process was performed successfully");
        errorAlert.showAndWait();
    }

    public void changeToDarkMode() {
        Scroll.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        Scroll.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        Scroll.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        Scroll.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    @FXML
    void clickClearButton(ActionEvent event) throws IOException {
        initLoansCheck();
        customerTabController.setPaymentTab();
        setEditable();
    }

    public void setEditable(){
        LoanerLoansTable.setEditable(true);
    }



    public void changeIfRewind(Boolean bool){
        LoanerLoansTable.setEditable(!bool);
        ButtonPayAll.setDisable(bool);
        ButtonPayPulse.setDisable(bool);
        ConveyMoneyOnRiskButton.setDisable(bool);
        refreshButtton.setDisable(bool);
        clearButton.setDisable(bool);

    }


    private void updateNotificationOnPaymentTab(DTOcustomer c){
        Platform.runLater(() -> {
            if(c != null) {
                NotificationArea.getItems().setAll(c.getRiskNotifications());
            }
        });
    }

    public void startNotificationsRefresher() {
        customerRefresher = new CustomerRefresher(
                customerTabController.getAutoUpdate(),
                this::updateNotificationOnPaymentTab,
                CUSTOMER);
        timer = new Timer();
        timer.schedule(customerRefresher, REFRESH_RATE, REFRESH_RATE);
    }


}
