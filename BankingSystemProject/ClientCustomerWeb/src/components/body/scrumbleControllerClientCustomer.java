package components.body;

import dtos.DTOLoan;
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
import loans.status.StatusENUM;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.controlsfx.control.CheckComboBox;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static components.app.appControllerClientCustomer.getError;
import static utils.Constants.GSON_INSTANCE;

public class scrumbleControllerClientCustomer {

    private BodyControllerClientCustomer customerTabController = null;
    @FXML private ScrollPane scrumbleS;
    @FXML private CheckBox CBcategoriesID;
    @FXML private CheckBox CBinterestID;
    @FXML private CheckBox CByazID;
    @FXML private CheckBox CBmaxLoansID;
    @FXML private CheckBox CBmaxPrecentID;
    @FXML private CheckComboBox<String> ComboBoxCategories;
    @FXML private TextField TextInterest;
    @FXML private TextField TextYaz;
    @FXML private TextField TextMaxLoans;
    @FXML private TextField TextMaxPrecent;
    @FXML private TextField amountTextID;
    @FXML private TableView<DTOLoan> table;
    @FXML private TableColumn<DTOLoan, Boolean> choiceID;
    @FXML private TableColumn<DTOLoan, String> IdLoan;
    @FXML private TableColumn<DTOLoan, String> loanOwner;
    @FXML private TableColumn<DTOLoan, String> category;
    @FXML private TableColumn<DTOLoan, Integer> loanAmount;
    @FXML private TableColumn<DTOLoan, Integer> totalYazTime;
    @FXML private TableColumn<DTOLoan, Integer> paysEveryYaz;
    @FXML private TableColumn<DTOLoan, Integer> interest;
    @FXML private TableColumn<DTOLoan, StatusENUM> loanStatus;
    @FXML private TableColumn<DTOLoan, Integer> totalFund;
    @FXML private TableColumn<DTOLoan, Integer> fundLeft;
    @FXML private TableColumn<DTOLoan, Integer> startActiveYaz;
    @FXML private TableColumn<DTOLoan, Integer> endingYaz;
    @FXML private Button finishedButton;
    @FXML private Button saveButton;
    private ObservableList<String> CategoriesList;
    private int minimumInterest;
    private int minimumYaz;
    private int maximumLoans;
    private int maximumPercent;
    private ObservableList<DTOLoan> filterLoans;


    @FXML
    public void initialize(){
        table.setEditable(true);
        IdLoan.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("IdLoan"));
        loanOwner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("loanOwner"));
        category.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("category"));
        loanAmount.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("loanAmount"));
        totalYazTime.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalYazTime"));
        paysEveryYaz.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("paysEveryYaz"));
        startActiveYaz.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("startActiveYaz"));
        endingYaz.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("endingYaz"));
        interest.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("interest"));
        loanStatus.setCellValueFactory(new PropertyValueFactory<DTOLoan, StatusENUM>("loanStatus"));
        fundLeft.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("fundLeft"));
        totalFund.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalFund"));
        choiceID.setCellFactory(col -> {
            CheckBoxTableCell<DTOLoan, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty checked = new SimpleBooleanProperty(table.getItems().get(index).getCheckProperty());
                checked.addListener((obs, NotSelected, selected) -> {
                    DTOLoan item = table.getItems().get(index);
                    item.setSelected(selected);
                });
                return checked ;
            });
            return cell ;
        });


    }

    @FXML void CBCategoriesListener(ActionEvent event) {
        boolean isSelected = CBcategoriesID.isSelected();
        ComboBoxCategories.setDisable(!isSelected);
        String getCategories = HttpUrl
                .parse(Constants.CATEGORIES)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsync(getCategories, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("Unable to get categories.")
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
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            String[] categories = GSON_INSTANCE.fromJson(rawBody, String[].class);
                            ComboBoxCategories.getItems().setAll(categories);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
    }

    @FXML
    void CBinterestListener(ActionEvent event) {
        boolean isSelected = CBinterestID.isSelected();
        TextInterest.setDisable(!isSelected);
    }

    @FXML
    void CBmaxLoansListener(ActionEvent event) {
        boolean isSelected = CBmaxLoansID.isSelected();
        TextMaxLoans.setDisable(!isSelected);
    }

    @FXML
    void CBmaxPrecentListener(ActionEvent event) {
        boolean isSelected = CBmaxPrecentID.isSelected();
        TextMaxPrecent.setDisable(!isSelected);
    }

    @FXML
    void CByazListener(ActionEvent event) {
        boolean isSelected = CByazID.isSelected();
        TextYaz.setDisable(!isSelected);
    }


    public void setMainController(BodyControllerClientCustomer customerTabController){
        this.customerTabController = customerTabController;
    }

    @FXML
    void saveFilterListener(ActionEvent event) {

        //ProgressDialog progressDialog
        initFilter();

        if(CBcategoriesID.isSelected()){
            this.CategoriesList = ComboBoxCategories.getCheckModel().getCheckedItems();
        }
        if(CBinterestID.isSelected()){
            this.minimumInterest = Integer.parseInt(TextInterest.getText());
        }
        if(CByazID.isSelected()){
            this.minimumYaz = Integer.parseInt(TextYaz.getText());
        }
        if(CBmaxLoansID.isSelected()){
            this.maximumLoans = Integer.parseInt(TextMaxLoans.getText());
        }
        if(CBmaxPrecentID.isSelected()){

            this.maximumPercent = Integer.parseInt(TextMaxPrecent.getText());

            if (maximumPercent < 0 || maximumPercent > 100){
                getErrorWrongOutputException();
                maximumPercent = 0;
            }
        }

        //Check if the amount on the label is a number
        boolean isFilteredList = checkLabelAmountAndCreateFilteredList();
        if(isFilteredList){
            finishedButton.setDisable(false);
        }

    }

    @FXML
    void FinishListner(ActionEvent event) throws  IOException {

        String chosen_loans = GSON_INSTANCE.toJson(createChosenLoanList());
        String scrumble = HttpUrl
                .parse(Constants.SCRUMBLING)
                .newBuilder()
                .addQueryParameter("chosen-loans", chosen_loans)
                .addQueryParameter("amount", amountTextID.getText())
                .addQueryParameter("max-precent", String.valueOf(maximumPercent))
                .build()
                .toString();

        HttpClientUtils.runAsync(scrumble, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("Unbale to scrumbling.")
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
                        getDoneMessage();
                        customerTabController.setScrumbleTabControllerToNull();
                        try {
                            /*customerTabController.getCustomer();*/
                            customerTabController.setScrumbleTab();
                            customerTabController.setInformationTab();
                            customerTabController.setPaymentTab();
                            customerTabController.setSellLoanTab();
                            customerTabController.setBuyLoanTab();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public List<DTOLoan> createChosenLoanList(){
        List<DTOLoan> newList = new ArrayList<>();
        for(DTOLoan l : filterLoans){
            if (l.getCheckProperty()){
                newList.add(l);
                l.setSelected(false);
            }
        }
        return newList;
    }

    public boolean checkLabelAmountAndCreateFilteredList()
    {
        try {

            if (amountTextID.getText().trim().isEmpty()) {
                getErrorWrongOutputException();
                return false;
            } else if (customerTabController.getCustomer().getBalance() >= Integer.parseInt(amountTextID.getText())) {
                String categoriesListJson = GSON_INSTANCE.toJson(CategoriesList);
                String getFilteredList = HttpUrl
                        .parse(Constants.FILTERED_LIST)
                        .newBuilder()
                        .addQueryParameter("categories-list", categoriesListJson)
                        .addQueryParameter("min-interest", String.valueOf(minimumInterest))
                        .addQueryParameter("min-yaz", String.valueOf(minimumYaz))
                        .addQueryParameter("max-loans", String.valueOf(maximumLoans))
                        .build()
                        .toString();

                HttpClientUtils.runAsync(getFilteredList, new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() ->
                                getError("Unable to filter loans.")
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
                                String rawBody = null;
                                try {
                                    rawBody = response.body().string();
                                    DTOLoan[] loans = GSON_INSTANCE.fromJson(rawBody, DTOLoan[].class);
                                    filterLoans = FXCollections.observableList(Arrays.asList(loans));
                                    table.setItems(filterLoans);
                                    table.setDisable(false);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            });
                        }
                    }
                });


            } else {
                getErrorLessMoneyException();
            }
        }
        catch(RuntimeException e)
        {
            getErrorWrongOutputException();
            return false;
        }
        return true;
    }


    public void initFilter(){
        this.CategoriesList = null;
        this.minimumInterest = 0;
        this.minimumYaz = 0;
        this.maximumLoans = 0;
        this.maximumPercent = 0;

    }

    public void getErrorWrongOutputException()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("The amount is wrong! please enter a number");
        errorAlert.showAndWait();
    }

    public void getErrorLessMoneyException()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("You don't have enough money in your account");
        errorAlert.showAndWait();
    }

    public void changeToDarkMode(){
        scrumbleS.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        scrumbleS.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        scrumbleS.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        scrumbleS.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    public static void getDoneMessage() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("Done!");
        errorAlert.setContentText("The process was performed successfully");
        errorAlert.showAndWait();
    }

    public void changeIfRewind(Boolean bool){
        table.setEditable(!bool);
        CBcategoriesID.setDisable(bool);
        CBinterestID.setDisable(bool);
        CByazID.setDisable(bool);
        CBmaxLoansID.setDisable(bool);
        CBmaxPrecentID.setDisable(bool);
        ComboBoxCategories.setDisable(bool);
        finishedButton.setDisable(bool);
        saveButton.setDisable(bool);
    }
}
