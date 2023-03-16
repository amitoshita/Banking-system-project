package components.body;

import components.app.appControllerAdmin;
import dtos.DTOLoan;
import dtos.DTOcustomer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientAdminUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import static components.app.appControllerAdmin.getError;
import static utils.Constants.*;
import static utils.Constants.REFRESH_RATE;

public class BodyControllerAdmin {

    private appControllerAdmin mainController;
    private loansInfoController loansInfoController;
    private customerInfoController customerInfoController;
    @FXML private Button IncreaseYazButton;
    @FXML private AnchorPane loanInfoOnAdmin;
    @FXML private ScrollPane loanInfoOnAdminScrollPane;
    @FXML private ScrollPane customersInfoOnAdminScrollPane;
    @FXML private AnchorPane customersInfoOnAdmin;
    @FXML private ScrollPane bodyScrollPane;
    private CustomersAdminRefresher customersAdminRefresher;
    private LoansListAdminRefresher loansListAdminRefresher;
    private Timer timer;
    private final BooleanProperty autoUpdate = new SimpleBooleanProperty();

    @FXML
    public void addYazAdmin(ActionEvent event) {

        String increaseYaz = HttpUrl
                .parse(INCREASE_YAZ)
                .newBuilder()
                .addQueryParameter("action", "add")
                .build()
                .toString();

        HttpClientAdminUtils.runAsync(increaseYaz, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("Increase yaz failed, try again.")
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
                            mainController.ChangeYazOnHeader(GSON_INSTANCE.fromJson((response.body().string()), Integer.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }


    public void setMainController(appControllerAdmin mainController){
        this.mainController = mainController;
        autoUpdate.bind(mainController.getAutoUpdateFromHeader());

    }

    public void showLoansInformation() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(LOANS_INFO_FXML);
        fxmlLoader.setLocation(url);
        AnchorPane TableLoansRoot = fxmlLoader.load(url.openStream());
        loansInfoController = fxmlLoader.getController();
        loansInfoController.setBodyController(this);
        loanInfoOnAdmin.getChildren().setAll(TableLoansRoot);
    }

    public void setIncreaseYazButtonToEnable(){
        IncreaseYazButton.setDisable(false);
    }

    public void showCustomersInfo() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(CUSTOMERS_INFO_FXML);
        fxmlLoader.setLocation(url);
        AnchorPane TableCustomersRoot = fxmlLoader.load(url.openStream());
        customerInfoController = fxmlLoader.getController();
        customerInfoController.setBodyController(this);
        customersInfoOnAdmin.getChildren().setAll(TableCustomersRoot);
    }

    private void updateCustomersList(List<DTOcustomer> customerList) {
        Platform.runLater(() -> {
            ObservableList<DTOcustomer> items = FXCollections.observableArrayList();
            items.addAll(customerList);
            try {
                if(((!customerInfoController.getIfParamExpended()) && (!mainController.getRewind()))||(mainController.getYazInRewind()== mainController.getCurrYaz()))
                {
                    customerInfoController.setCustomersList(items);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startCustomersListRefresher() {
        customersAdminRefresher = new CustomersAdminRefresher(
                autoUpdate,
                this::updateCustomersList,
                CUSTOMERS_LIST);
        timer = new Timer();
        timer.schedule(customersAdminRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateLoansList(List<DTOLoan> loanList) {
        Platform.runLater(() -> {
            ObservableList<DTOLoan> items = FXCollections.observableArrayList();
            items.addAll(loanList);
            try {
                if(!mainController.getRewind() || (mainController.getYazInRewind()== mainController.getCurrYaz())) {
                    loansInfoController.setLoanList(items);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startLoansListRefresher() {
        loansListAdminRefresher = new LoansListAdminRefresher(
                autoUpdate,
                this::updateLoansList,
                LOANS_LIST);
        timer = new Timer();
        timer.schedule(loansListAdminRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void changeToLightMode()  {
        bodyScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        bodyScrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    public void changeToDarkMode()  {
        bodyScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        bodyScrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public boolean isItsDarkMode(){
        return mainController.isIfItsDarkMode();
    }

    public void changeIfRewind(Boolean bool){
        IncreaseYazButton.setDisable(bool);
    }
    public void setLoansTableListByYaz(int yaz){

        String finalUrl = HttpUrl
                .parse(LOANS_LIST)
                .newBuilder()
                .addQueryParameter("rewind", "true")
                .addQueryParameter("yaz", String.valueOf(yaz))
                .build()
                .toString();

        HttpClientAdminUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in getting loans info.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            DTOLoan[] loans = GSON_INSTANCE.fromJson(rawBody, DTOLoan[].class);
                            ObservableList<DTOLoan> items = FXCollections.observableArrayList();
                            if(loans!=null){
                            items.addAll(loans);
                            }
                            loansInfoController = null;
                            showLoansInformation();
                            loansInfoController.setLoanList(items);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });

    }

    public void setCustomersTableListByYaz(int yaz){

        String customersURL = HttpUrl
                .parse(CUSTOMERS_LIST)
                .newBuilder()
                .addQueryParameter("rewind", "true")
                .addQueryParameter("yaz", String.valueOf(yaz))
                .build()
                .toString();

        HttpClientAdminUtils.runAsync(customersURL, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in getting customers info.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            DTOcustomer[] customers = GSON_INSTANCE.fromJson(rawBody, DTOcustomer[].class);
                            ObservableList<DTOcustomer> items = FXCollections.observableArrayList();
                            if(customers!=null){
                                items.addAll(customers);
                            }
                            customerInfoController = null;
                            showCustomersInfo();
                            customerInfoController.setCustomersList(items);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });

    }
}
