package components.app;

import components.body.BodyControllerAdmin;
import components.header.headerControllerAdmin;
import components.login.loginAdminController;
import dtos.DTOcustomer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientAdminUtils;

import java.io.IOException;
import java.util.Arrays;

import static utils.Constants.*;

public class appControllerAdmin {
    @FXML ScrollPane appScrollPane;
    @FXML AnchorPane anchorPaneCenter;
    @FXML BorderPane MainborderPane;
    @FXML private ScrollPane loginComponent;
    @FXML private ScrollPane headerComponent;
    @FXML private loginAdminController loginComponentController;
    @FXML private headerControllerAdmin headerComponentController;
    private BodyControllerAdmin bodyControllerAdmin;
    private ScrollPane bodyCustomerComponent;
    private String currentUserName;
    private boolean ifItsDarkMode = false;
    private int yazInRewind = 1;


    @FXML
    public void initialize() throws IOException {

        if (headerComponentController != null && loginComponentController != null) {
            headerComponentController.setMainController(this);
            loginComponentController.setMainController(this);
        }
    }

    public void setFXMLData() throws IOException {
        FXMLLoader FXMLLoaderBody = new FXMLLoader(getClass().getResource(Constants.BODY_FXML));
        FXMLLoaderBody.setLocation(getClass().getResource(Constants.BODY_FXML));
        this.bodyCustomerComponent = (ScrollPane) FXMLLoaderBody.load();
        this.bodyControllerAdmin = FXMLLoaderBody.getController();
        this.bodyControllerAdmin.setMainController(this);
    }


    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }


    public static void getError(String message)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("Something went wrong." + message);
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
        bodyControllerAdmin.showLoansInformation();
        bodyControllerAdmin.showCustomersInfo();
        bodyControllerAdmin.setIncreaseYazButtonToEnable();
    }


    public void changeToDarkMode()  {
        this.ifItsDarkMode = true;
        appScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        appScrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        headerComponentController.changeToDarkMode();
        loginComponentController.changeToDarkMode();
        bodyControllerAdmin.changeToDarkMode();
    }

    public void ChangeYazOnHeader(int YazTime){
        headerComponentController.ChangeYaz(YazTime);
    }

    public void changeToLightMode()  {
        this.ifItsDarkMode = false;
        appScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        appScrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
        headerComponentController.changeToLightMode();
        loginComponentController.changeToLightMode();
        bodyControllerAdmin.changeToLightMode();
    }

    public boolean isIfItsDarkMode() {
        return ifItsDarkMode;
    }

    public BooleanProperty getAutoUpdateFromHeader(){
        return headerComponentController.autoUpdatesProperty();
    }

    public void setInformationOfAdmin(){
        bodyControllerAdmin.startCustomersListRefresher();
        bodyControllerAdmin.startLoansListRefresher();
    }


    public static void getErrorJSON()
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Exception");
        errorAlert.setContentText("ERROR in JSON translation. Please try again");
        errorAlert.showAndWait();
    }

    public void setRewindMode(Boolean bool){
        String boolStr;
        if(bool){
            boolStr = "true";
        }
        else {
            boolStr = "false";
        }
        bodyControllerAdmin.changeIfRewind(bool);
        String rewindUrl = HttpUrl
                .parse(IS_REWIND)
                .newBuilder()
                .addQueryParameter("rewind", boolStr)
                .addQueryParameter("user-type", "admin")
                .build()
                .toString();

        HttpClientAdminUtils.runAsync(rewindUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in getting loans info.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    headerComponentController.setPlusAndMinus();
                }
            }
        });

    }
    public Boolean getRewind() {
        return headerComponentController.getRewind();
    }

    public void setLoansTableListByYaz(int yaz){
        bodyControllerAdmin.setLoansTableListByYaz(yaz);
    }

    public void setCustomersTableListByYaz(int yaz){
        bodyControllerAdmin.setCustomersTableListByYaz(yaz);
    }

    public int getYazInRewind(){
        return headerComponentController.getYazInRewind();
    }

    public int getCurrYaz(){
        return headerComponentController.getCurrYaz();
    }
}
