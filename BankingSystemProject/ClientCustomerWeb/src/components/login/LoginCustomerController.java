package components.login;

import components.app.appControllerClientCustomer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;

import static components.app.appControllerClientCustomer.getError;
import static utils.Constants.LOGIN_PAGE;

public class LoginCustomerController {


    private appControllerClientCustomer appControllerClientCustomer;
    @FXML private ImageView logoImage;
    @FXML private Label labelName;
    @FXML private TextField TextName;
    @FXML private ScrollPane scrollPane;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    public void setMainController(appControllerClientCustomer mainController){
        this.appControllerClientCustomer = mainController;
    }

    @FXML public void initialize() {

    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = TextName.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        // יוצרים URL המתאים לקטע הservlet הלוגין
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("user-type", "Customer")
                .build()
                .toString();


        // שליחת הבקשה לשרת
        HttpClientUtils.runAsync(finalUrl, new Callback() {

            // אם המשתמש לא מצליח להתחבר למערכת
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("Login failed, try again.")
                );
            }

            // אם המשתמש מצליח להתחבר למערכת
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            getError(responseBody)

                    );
                } else {
                    Platform.runLater(() -> {
                            appControllerClientCustomer.setCurrentUserName(userName);
                            try {
                                appControllerClientCustomer.switchScreenToBody();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            appControllerClientCustomer.setCustomersList();

                    });
                }
            }
        });
    }
    public void changeToDarkMode()  {
        scrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }


    public void changeToLightMode()  {
        scrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }


}
