package body.customer;

import dtos.DTO;
import dtos.DTOLoan;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import loans.status.StatusENUM;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrumbleCustomerController {

    private CustomerTabController customerTabController = null;
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
        ComboBoxCategories.getItems().add("Setup a business");
        ComboBoxCategories.getItems().add("Overdraft cover");
        ComboBoxCategories.getItems().add("Investment");
        ComboBoxCategories.getItems().add("Happy Event");
        ComboBoxCategories.getItems().add("Renovate");

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


    public void setMainController(CustomerTabController customerTabController){
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
    void FinishListner(ActionEvent event) throws InterruptedException, IOException {
        customerTabController.conveyTaskDataToAppController(createChosenLoanList(), Integer.parseInt(amountTextID.getText()), maximumPercent);
        getDoneMessage();
        customerTabController.setScrumbleTabControllerToNull();
        customerTabController.setScrumbleTab();
        customerTabController.setInformationTab();
        customerTabController.setPaymentTab();
        customerTabController.updateAdminTablesToAppController();
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
            } else if (customerTabController.getCustomerBalance() >= Integer.parseInt(amountTextID.getText())) {
                filterLoans = customerTabController.getFilterList(CategoriesList, minimumInterest, minimumYaz, maximumLoans);
                table.setItems(filterLoans);
                table.setDisable(false);
                return true;

            } else {
                getErrorLessMoneyException();
                return false;
            }
        }
        catch(RuntimeException e)
        {
            getErrorWrongOutputException();
            return false;
        }

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
        scrumbleS.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        scrumbleS.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        scrumbleS.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        scrumbleS.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
    }

    public void getDoneMessage() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("Done!");
        errorAlert.setContentText("The process was performed successfully");
        errorAlert.showAndWait();
    }



}
