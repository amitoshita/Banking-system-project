package app;

import body.BodyController;
import body.customer.CustomerTabController;

import dtos.DTOLoan;
import dtos.DTOcustomer;
import exceptions.fileCategoriesException;
import exceptions.fileCustomersNamesException;
import exceptions.fileDivideYazPaymentException;
import exceptions.fileExtentionException;
import general.Transport;
import header.HeaderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.controlsfx.dialog.ProgressDialog;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;


public class AppController {


    public static final String CUSTOMER_TAB_FXML = "/body/customer/customerTabFxml.fxml";
    public static final String ADMIN_TAB_FXML = "/body/bodyFxml.fxml";
    @FXML ScrollPane appScrollPane;
    @FXML AnchorPane anchorPaneCenter;
    @FXML BorderPane MainborderPane;
    @FXML private ScrollPane bodyAdminComponent;
    @FXML private BodyController bodyAdminComponentController;
    private CustomerTabController bodyCustomerController;
    @FXML private HeaderController headerComponentController;
    private ScrollPane bodyCustomerComponent;
    private Transport transport;
    private String xmlFile;
    private boolean isFileUploaded = false;
    private String currentUserName;
    private Task<Boolean> currentTask;
    private boolean ifItsDarkMode = false;


    @FXML
    public void initialize() throws IOException {

        if (headerComponentController != null && bodyAdminComponentController != null) {
            headerComponentController.setMainController(this);
            bodyAdminComponentController.setMainController(this);
        }
        if (isFileUploaded = false) {
            bodyAdminComponentController.setButtonsToDisable();
        }

    }

    public void loadFileOnLabel(String s) {
        this.xmlFile = s;
        loadXMLFile();
        //headerComponentController.loadFileOnLabel(s);
    }

    public void loadXMLFile() {
        try {
            transport = new Transport();
            transport.loadNewXMLFile(this.xmlFile.toString());
            isFileUploaded = true;
            headerComponentController.loadFileOnLabel(this.xmlFile.toString());
            setFXMLData();
            bodyAdminComponentController.setButtonsToEnable();
            bodyAdminComponentController.showLoansInformation();
            bodyAdminComponentController.showCustomersInfo();
            sendAllCustomersToHeader();
            headerComponentController.setToggleOnEnable();
        } catch (FileNotFoundException e) {
            //bodyAdminComponentController.setIsFileLoadedLabel("File not found!");
        } catch (fileExtentionException e) {
            //bodyAdminComponentController.setIsFileLoadedLabel(e.getMessage());
        } catch (JAXBException e) {
            //bodyAdminComponentController.setIsFileLoadedLabel("File is not good!");
        } catch (fileCategoriesException e) {
            this.getErrorOfCategories();
            //bodyAdminComponentController.setIsFileLoadedLabel(e.getMessage());
        } catch (fileCustomersNamesException e) {
            this.getErrorCustomersNamesException();
            //bodyAdminComponentController.setIsFileLoadedLabel(e.getMessage());
        } catch (fileDivideYazPaymentException e) {
            getErrorDivideYazPaymentException();
            //bodyAdminComponentController.setIsFileLoadedLabel(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAllCustomersToHeader() {
        headerComponentController.setComboBoxItems(transport.getDTOCustomers().getAllCustomers());
    }

    public void changeCurrYaz() throws IOException {
        transport.addLoanYaz();
        transport.checkIfLoanIsRisk();
        updateAdminTablesBodyController();
        headerComponentController.ChangeYaz((transport.getLoanYaz()));
    }

    public ObservableList<DTOLoan> getDTOLoansInfo() {
        return transport.getObservableLoanList();
    }

    public ObservableList<DTOcustomer> getDTOCustomerInfo() {
        return transport.getObservableCustomersList();
    }

    public void setFXMLData() throws IOException {
        FXMLLoader FXMLLoaderAdmin = new FXMLLoader(getClass().getResource(ADMIN_TAB_FXML));
        FXMLLoader FXMLLoaderCustomer = new FXMLLoader(getClass().getResource(CUSTOMER_TAB_FXML));
        FXMLLoaderAdmin.setLocation(getClass().getResource(ADMIN_TAB_FXML));
        FXMLLoaderCustomer.setLocation(getClass().getResource(CUSTOMER_TAB_FXML));
        this.bodyCustomerComponent = (ScrollPane) FXMLLoaderCustomer.load();
        this.bodyCustomerController = FXMLLoaderCustomer.getController();
        this.bodyCustomerController.setMainController(this);

    }
    public void switchScreenToCustomer() throws IOException {
        if (anchorPaneCenter.getChildren() != null) {
            anchorPaneCenter.getChildren().removeAll();
        }
        anchorPaneCenter.getChildren().setAll(bodyCustomerComponent);
        anchorPaneCenter.setRightAnchor(bodyCustomerComponent,0.0);
        anchorPaneCenter.setLeftAnchor(bodyCustomerComponent, 0.0);
        MainborderPane.setCenter(anchorPaneCenter);
        bodyCustomerController.setInformationTab();
        bodyCustomerController.setScrumbleTab();
        bodyCustomerController.setPaymentTab();

    }

    public void switchScreenToAdmin() {
        if (anchorPaneCenter.getChildren() != null) {
            anchorPaneCenter.getChildren().removeAll();
        }
        anchorPaneCenter.getChildren().setAll(bodyAdminComponent);
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public ObservableList<DTOLoan> getLoanerLoansByName(){
        return transport.getObservableLoanerListByName(currentUserName);
    }

    public ObservableList<DTOLoan> getLendersLoansByName(){
        return transport.getObservableLendersListByName(currentUserName);
    }

    public ObservableList<DTOLoan> filterLoansByNameStatus() {
        Predicate<DTOLoan> RiskActivePredicate = (p) -> (p.getLoanStatus().getName().equals("risk") || p.getLoanStatus().getName().equals("active"));
        return FXCollections.observableArrayList(getLoanerLoansByName().stream().filter(RiskActivePredicate).collect(Collectors.toList()));
    }

    public ObservableList<DTOLoan> filterLoansScrumble(ObservableList<String> CategoriesList, int minimumInterest,int minimumYaz, int maximumLoans){
        //return (transport.filterMatchLoan(CategoriesList, minimumInterest, minimumYaz, maximumLoans, this.currentUserName));
        return null ;
    }



    public DTOcustomer getCustomer(){
        return transport.getCustomerByHisName(this.currentUserName);
    }

    public void addMoneyToCurrCustomerByTransport(String money){
        transport.addMoneyToCustomer(currentUserName, Integer.parseInt(money));
    }

    public void minusMoneyToCurrCustomerByTransport(String money){
        transport.minusMoneyToCustomer(currentUserName, Integer.parseInt(money));
    }

    public void getErrorOfCategories()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("The file's categories is not according the format.");
        errorAlert.showAndWait();
    }

    public void getErrorCustomersNamesException()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("There is a name that returns a few times. ");
        errorAlert.showAndWait();
    }

    public void getErrorDivideYazPaymentException()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("One of the loan's total yaz not dividing in yaz per payment");
        errorAlert.showAndWait();
    }

    public int getLoansAmountFromTransportBorrowers(String status, String currentName){
        return transport.countBorrowerLoansByStatus((status), currentName);
    }

    public int getLoansAmountFromTransportLenders(String status, String currentUserName){
        return transport.countLendersLoansByStatus(status, currentUserName);
    }

    public void scrumbleTasking(List<DTOLoan> loanList, int amount, int precent) throws InterruptedException {
        this.currentTask = new ScrumbleTask(loanList, currentUserName, amount, this, precent);
        ProgressDialog progressDialog = new ProgressDialog(currentTask);
        progressDialog.setHeaderText("Running operation");
        progressDialog.setTitle("Scrumble loans");
        new Thread(currentTask).start();
        progressDialog.showAndWait();
    }

    public Transport getTransport(){
        return transport;
    }

    public boolean payOnePulseByLoan (DTOLoan loan){
        return transport.conveyMoneyToLoanLenders(loan);
    }

    public boolean payAllLoan(DTOLoan loan){
        return transport.conveyAllMoneyToLoanLenders(loan);
    }

    public void updateAdminTablesBodyController() throws IOException {
        bodyAdminComponentController.showCustomersInfo();
        bodyAdminComponentController.showLoansInformation();
        if(isIfItsDarkMode()){
            bodyAdminComponentController.changeToDarkMode();
        }
    }

    public void changeToDarkMode()  {
        this.ifItsDarkMode = true;
        appScrollPane.getStylesheets().removeAll(getClass().getResource("appLightMode.css").toExternalForm());
        appScrollPane.getStylesheets().add(getClass().getResource("appDarkMode.css").toExternalForm());
        headerComponentController.changeToDarkMode();
        bodyAdminComponentController.changeToDarkMode();
        bodyCustomerController.changeToDarkMode();
    }

    public void changeToLightMode()  {
        this.ifItsDarkMode = false;
        appScrollPane.getStylesheets().removeAll(getClass().getResource("appDarkMode.css").toExternalForm());
        appScrollPane.getStylesheets().add(getClass().getResource("appLightMode.css").toExternalForm());
        headerComponentController.changeToLightMode();
        bodyAdminComponentController.changeToLightMode();
        bodyCustomerController.changeToLightMode();
    }

    public boolean isIfItsDarkMode() {
        return ifItsDarkMode;
    }

    public boolean conveyMoneyToRiskLoanAppController(int amount, DTOLoan loan){
        return transport.conveyMoneyToRiskLoan(amount, loan);
    }
}





