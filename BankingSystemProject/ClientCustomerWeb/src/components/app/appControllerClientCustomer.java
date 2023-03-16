package components.app;

import components.body.BodyControllerClientCustomer;
import components.body.CurrYazRefresher;
import components.body.IsRewindRefresher;
import components.header.headerControllerClientCustomer;
import components.login.LoginCustomerController;
import dtos.DTOLoan;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import static utils.Constants.REFRESH_RATE;


public class appControllerClientCustomer {


    @FXML
    ScrollPane appScrollPane;
    @FXML AnchorPane anchorPaneCenter;
    @FXML BorderPane MainborderPane;
    @FXML private ScrollPane loginComponent;
    @FXML private ScrollPane headerComponent;
    @FXML private LoginCustomerController loginComponentController;
    @FXML private headerControllerClientCustomer headerComponentController;
    private BodyControllerClientCustomer bodyControllerClientCustomer;
    private ScrollPane bodyCustomerComponent;
    private String currentUserName;
    private boolean ifItsDarkMode = false;
    public IsRewindRefresher isRewindRefresher;
    private Timer timer;

    @FXML
    public void initialize() throws IOException {

        if (headerComponentController != null && loginComponentController != null) {
            headerComponentController.setMainController(this);
            loginComponentController.setMainController(this);
        }

    }

    public void setFXMLData() throws IOException {
        FXMLLoader FXMLLoaderBody = new FXMLLoader(getClass().getResource(Constants.BODY_TAB_FXML));
        FXMLLoaderBody.setLocation(getClass().getResource(Constants.BODY_TAB_FXML));
        this.bodyCustomerComponent = (ScrollPane) FXMLLoaderBody.load();
        this.bodyControllerClientCustomer = FXMLLoaderBody.getController();
        this.bodyControllerClientCustomer.setMainController(this);
    }


    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
        headerComponentController.setHelloLabel(currentUserName);
    }


    public static void getError(String message)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("Something went wrong." + message);
        errorAlert.showAndWait();
    }

    public void getErrorJSON()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("ERROR in JSON translation. Please try again");
        errorAlert.showAndWait();
    }


    public void switchScreenToBody() throws IOException {
        if (anchorPaneCenter.getChildren() != null) {
            anchorPaneCenter.getChildren().removeAll();
        }
        setFXMLData();
        headerComponentController.setButtonsToEnable();
        anchorPaneCenter.getChildren().setAll(bodyCustomerComponent);
        anchorPaneCenter.setRightAnchor(bodyCustomerComponent,0.0);
        anchorPaneCenter.setLeftAnchor(bodyCustomerComponent, 0.0);
        MainborderPane.setCenter(anchorPaneCenter);
        setBodyTabs();
    }

    public void setBodyTabs() throws IOException {
        /*bodyControllerClientCustomer.getCustomer();*/
        bodyControllerClientCustomer.setInformationTab();
        bodyControllerClientCustomer.setScrumbleTab();
        bodyControllerClientCustomer.setCreateLoanClientCustomer();
        bodyControllerClientCustomer.setPaymentTab();
        bodyControllerClientCustomer.setSellLoanTab();
        bodyControllerClientCustomer.setBuyLoanTab();
    }


    public void changeToDarkMode()  {
        this.ifItsDarkMode = true;
        appScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        appScrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        headerComponentController.changeToDarkMode();
        loginComponentController.changeToDarkMode();
        bodyControllerClientCustomer.changeToDarkMode();
    }

    public void changeToLightMode()  {
        this.ifItsDarkMode = false;
        appScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        appScrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
        headerComponentController.changeToLightMode();
        loginComponentController.changeToLightMode();
        bodyControllerClientCustomer.changeToLightMode();
    }

    public boolean isIfItsDarkMode() {
        return ifItsDarkMode;
    }

    public void setCustomersList(){
        headerComponentController.startListRefresher();
        setInformationOfCustomer();
    }

    public BooleanProperty getAutoUpdateFromHeader(){
        return headerComponentController.autoUpdatesProperty();
    }

    public void setInformationOfCustomer(){
        bodyControllerClientCustomer.startMyLoansListRefresher();
        bodyControllerClientCustomer.startMyInvestedLoansRefresher();
        bodyControllerClientCustomer.startMyLoansOnRiskAndActiveOnPaymentTabRefresher();
        bodyControllerClientCustomer.startBankActionRefresher();
        bodyControllerClientCustomer.startBankCustomerBalanceRefresher();
        bodyControllerClientCustomer.startCustomerRefresher();
        bodyControllerClientCustomer.startLoansListRefresher();
        bodyControllerClientCustomer.startLoansPartToBuyRefresher();
        headerComponentController.startYazTimeRefresher();
        startRewindRefresher();
    }


    private void updateRewind(Boolean rewind){
        Platform.runLater(() -> {
            bodyControllerClientCustomer.changeIfRewind(rewind);
            headerComponentController.changeIfRewind(rewind);

        });
    }


    public void startRewindRefresher() {
        isRewindRefresher = new IsRewindRefresher(
                getAutoUpdateFromHeader(),
                this::updateRewind);
        timer = new Timer();
        timer.schedule(isRewindRefresher, REFRESH_RATE, REFRESH_RATE);
    }



}
