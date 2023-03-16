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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

import static components.app.appControllerClientCustomer.getError;
import static components.body.paymentControllerClientCustomer.getDoneMessage;
import static components.body.paymentControllerClientCustomer.getErrorException;
import static utils.Constants.GSON_INSTANCE;

public class sellLoanControllerClientCustomer {

    @FXML private ScrollPane sellLoanScroll;
    @FXML private TableView<DTOlender> table;
    @FXML private TableColumn<DTOlender, Boolean> choosen;
    @FXML private TableColumn<DTOlender, String> idLoan;
    @FXML private TableColumn<DTOlender, String> loanStatus;
    @FXML private TableColumn<DTOlender, String> loanOwner;
    @FXML private TableColumn<DTOlender, Integer> fundRest;
    @FXML private TableColumn<DTOlender, Integer> loanInterest;
    @FXML private TableColumn<DTOlender, Integer> loanPayEveryYaz;
    @FXML private Button sellLoanButton;
    @FXML private Button clearButton;
    private BodyControllerClientCustomer customerTabController;
    private ObservableList<DTOlender> loanList;
    private DTOlender chosenLoan;

    @FXML
    public void initialize() {
        table.setEditable(true);
        idLoan.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("idLoan"));
        fundRest.setCellValueFactory(new PropertyValueFactory<DTOlender, Integer>("fundRest"));
        loanInterest.setCellValueFactory(new PropertyValueFactory<DTOlender, Integer>("loanInterest"));
        loanOwner.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("loanOwner"));
        loanStatus.setCellValueFactory(new PropertyValueFactory<DTOlender, String>("loanStatus"));
        loanPayEveryYaz.setCellValueFactory(new PropertyValueFactory<DTOlender, Integer>("loanPayEveryYaz"));
        choosen.setCellFactory(col -> {
            CheckBoxTableCell<DTOlender, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty checked = new SimpleBooleanProperty(table.getItems().get(index).getCheckProperty());
                checked.addListener((obs, NotSelected, selected) -> {
                    DTOlender item = table.getItems().get(index);
                    item.setSelected(selected);
                    if (selected) {
                        sellLoanButton.setDisable(false);
                        clearButton.setDisable(false);
                    } else if (!selected) {
                        sellLoanButton.setDisable(true);
                        clearButton.setDisable(true);
                    }
                });
                return checked;
            });
            return cell;
        });
    }

    public void changeIfRewind(Boolean bool){
        table.setEditable(!bool);
        sellLoanButton.setDisable(bool);
        clearButton.setDisable(bool);
    }

    public void setLoansList(ObservableList<DTOlender> lst) throws IOException {
        if(lst != null) {
            loanList = FXCollections.observableList(lst);
            table.setItems(loanList);
        }
    }

    public ObservableList<DTOlender> getLoanList() {
        return loanList;
    }

    public void setMainController(BodyControllerClientCustomer customerTabController){
        this.customerTabController = customerTabController;
    }

    @FXML
    void clearButtonOnAction(ActionEvent event) throws IOException {
        initLoansCheck();
        customerTabController.setSellLoanTab();
        table.setEditable(true);
    }


    public void initLoansCheck(){
        for (DTOlender l : loanList) {
            if (l.getCheckProperty()) {
                chosenLoan = l;
                l.setSelected(false);

            }
        }
        table.setEditable(true);
    }

    @FXML
    void sellLoanButtonOnAction(ActionEvent event) throws IOException {
        initLoansCheck();
        if(chosenLoan == null){
            getError("Please choose one part to sell.");
            table.setEditable(true);
        }
        else
        {
            String chosenPartForSell = GSON_INSTANCE.toJson(chosenLoan);
            String sellLoanPart = HttpUrl
                    .parse(Constants.SELL_LOAN)
                    .newBuilder()
                    .addQueryParameter("chosen-part-to-sell", chosenPartForSell)
                    .build()
                    .toString();
            sendSellLoanToServer(sellLoanPart);
            initLoansCheck();
            customerTabController.setSellLoanTab();
            table.setEditable(true);
        }
    }

    public void sendSellLoanToServer(String sellLoanPart) throws IOException {
        HttpClientUtils.runAsync(sellLoanPart, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("FAILED to sell loan.")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            getErrorException("Unable to sell loan part...")

                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            initLoansCheck();
                            customerTabController.setSellTabControllerToNull();
                            customerTabController.setSellLoanTab();
                            customerTabController.setBuyLoanTab();
                            table.getItems().clear();
                            getDoneMessage();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

    }


    public void changeToDarkMode() {
        sellLoanScroll.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        sellLoanScroll.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        sellLoanScroll.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        sellLoanScroll.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }
}
