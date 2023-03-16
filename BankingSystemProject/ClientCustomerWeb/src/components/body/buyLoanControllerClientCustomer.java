package components.body;
import dtos.DTOLoan;
import dtos.DTOlender;
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

import static components.app.appControllerClientCustomer.getError;
import static components.body.paymentControllerClientCustomer.getDoneMessage;
import static components.body.paymentControllerClientCustomer.getErrorException;
import static utils.Constants.GSON_INSTANCE;

public class buyLoanControllerClientCustomer {

    @FXML private ScrollPane buyLoanScroll;
    @FXML private TableView<DTOlender> table;
    @FXML private TableColumn<DTOlender, Boolean> choosen;
    @FXML private TableColumn<DTOlender, String> idLoan;
    @FXML private TableColumn<DTOlender, String> lenderName;
    @FXML private TableColumn<DTOlender, String> loanOwner;
    @FXML private TableColumn<DTOlender, String> loanStatus;
    @FXML private TableColumn<DTOlender, Integer> fundRest;
    @FXML private TableColumn<DTOlender, Integer> loanInterest;
    @FXML private TableColumn<DTOlender, Integer> loanPayEveryYaz;
    @FXML private Button buyLoanButton;
    @FXML private Button clearButton;
    private BodyControllerClientCustomer customerTabController;
    private ObservableList<DTOlender> LendersList;


    @FXML
    public void initialize() {
        idLoan.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("idLoan"));
        loanOwner.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("loanOwner"));
        lenderName.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("lenderName"));
        loanStatus.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("loanStatus"));
        fundRest.setCellValueFactory(new PropertyValueFactory<DTOlender, Integer>("fundRest"));
        loanInterest.setCellValueFactory(new PropertyValueFactory<DTOlender, Integer>("loanInterest"));
        loanPayEveryYaz.setCellValueFactory(new PropertyValueFactory<DTOlender, Integer>("loanPayEveryYaz"));

        choosen.setCellFactory(col -> {
            CheckBoxTableCell<DTOlender, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty checked = new SimpleBooleanProperty(table.getItems().get(index).getCheckProperty());
                checked.addListener((obs, NotSelected, selected) -> {
                    DTOlender item = table.getItems().get(index);
                    item.setSelected(selected);
                    if (selected) {
                        buyLoanButton.setDisable(false);
                        clearButton.setDisable(false);
                    } else if (!selected) {
                        buyLoanButton.setDisable(true);
                        clearButton.setDisable(true);
                    }
                });
                return checked;
            });

            return cell;
        });
    }

    public void setLendersList(ObservableList<DTOlender> lst) throws IOException {
        if(lst != null) {
            LendersList = FXCollections.observableList(lst);
            table.setItems(LendersList);
        }
    }

    public ObservableList<DTOlender> getLoansPartList(){
        return LendersList;
    }

    public void setEditable(){
        table.setEditable(true);
    }


    @FXML void buyLoanButtonOnAction(ActionEvent event) {

        DTOlender loanLenderPartToBuy = theChosenPartForBuy();
        if(loanLenderPartToBuy == null){
            getError("Please choose one part to buy.");
            table.setEditable(true);
        }
        else
        {
            String chosenLenderPart = GSON_INSTANCE.toJson(loanLenderPartToBuy);
            String buyLoanPart = HttpUrl
                    .parse(Constants.BUY_LOAN)
                    .newBuilder()
                    .addQueryParameter("chosen-part-to-buy", chosenLenderPart)
                    .build()
                    .toString();
            if(customerTabController.getCustomer().getBalance() >= loanLenderPartToBuy.getFundRest()) {
                sendBuyLoanToServer(buyLoanPart);
            }
            else {
                getError("You do not have enough money to this action.");
            }
        }

    }

    public void sendBuyLoanToServer(String buyLoanPart){
        HttpClientUtils.runAsync(buyLoanPart, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("FAILED to buy loan.")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            getErrorException("Unable to buy loan part...")

                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            customerTabController.setBuyControllerToNull();
                            customerTabController.setInformationTab();
                            customerTabController.setBuyLoanTab();
                            getDoneMessage();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public DTOlender theChosenPartForBuy() {
        for (DTOlender l : LendersList) {
            if (l.getCheckProperty()) {
                l.setSelected(false);
                table.setEditable(true);
                return l;
            }
        }
        return null;
    }

    @FXML
    void clearButtonOnAction(ActionEvent event) throws IOException {
        initLendersCheck();
        customerTabController.setBuyLoanTab();
        table.setEditable(true);
    }

    public void initLendersCheck(){
        for (DTOlender l : LendersList) {
            if (l.getCheckProperty()) {
                l.setSelected(false);
            }
        }
    }

    public void setMainController(BodyControllerClientCustomer customerTabController){
        this.customerTabController = customerTabController;
    }


    public void changeToDarkMode() {
        buyLoanScroll.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        buyLoanScroll.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        buyLoanScroll.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        buyLoanScroll.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    public void changeIfRewind(Boolean bool) {
        table.setEditable(!bool);
        buyLoanButton.setDisable(bool);
        clearButton.setDisable(bool);
    }
}
