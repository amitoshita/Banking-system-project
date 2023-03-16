package components.login;

import javafx.application.Platform;
import components.app.appControllerAdmin;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientAdminUtils;

import java.io.IOException;

import static components.app.appControllerAdmin.getError;

public class loginAdminController {

    @FXML private ScrollPane scrollPane;
    @FXML private ImageView logoImage;
    @FXML private Label labelName;
    @FXML private TextField TextName;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private appControllerAdmin appControllerAdmin;


    public void setMainController(appControllerAdmin mainController){
        this.appControllerAdmin = mainController;
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = TextName.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("Admin user name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("user-type", "Admin")
                .build()
                .toString();

        HttpClientAdminUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getError("Login failed, try again.")
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
                        appControllerAdmin.setCurrentUserName(userName);
                        try {
                            appControllerAdmin.switchScreenToBody();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        appControllerAdmin.setInformationOfAdmin();

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
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }
}
