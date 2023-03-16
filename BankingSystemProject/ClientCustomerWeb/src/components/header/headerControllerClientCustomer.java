package components.header;

import components.app.appControllerClientCustomer;
import components.body.CurrYazRefresher;
import components.body.CustomerRefresher;
import dtos.DTOcustomer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import javax.jws.soap.SOAPBinding;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static components.app.appControllerClientCustomer.getError;
import static java.lang.String.valueOf;
import static javax.xml.transform.OutputKeys.MEDIA_TYPE;
import static utils.Constants.*;
import static utils.HttpClientUtils.HTTP_CLIENT;

public class headerControllerClientCustomer implements Closeable {


    private appControllerClientCustomer mainController;
    @FXML private Label currYazTimeLabel;
    @FXML private ToggleSwitch modeSwitch;
    @FXML private ScrollPane headerScrollPane;
    @FXML private VBox customersVbox;
    @FXML private Label usersLabel;
    @FXML private Button LoadFileButton;
    @FXML private Label helloLabel;
    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;
    @FXML private ListView<String> customersListView = new ListView<>();
    private ObservableList<String> items;
    @FXML private ToggleButton autoUpdatesButton;
    private CurrYazRefresher currYazRefresher;
    @FXML private Label rewindMode;


    public headerControllerClientCustomer() {
        autoUpdate = new SimpleBooleanProperty();
        totalUsers = new SimpleIntegerProperty();
    }

    @FXML
    public void initialize() {
        modeSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                mainController.changeToDarkMode();
            } else {
                mainController.changeToLightMode();
            }
        });
        usersLabel.textProperty().bind(Bindings.concat("Chat Users: (", totalUsers.asString(), ")"));
        autoUpdate.bind(autoUpdatesButton.selectedProperty());
    }


    public void setMainController(appControllerClientCustomer mainController){
        this.mainController = mainController;
    }

    public void setButtonsToEnable(){
        this.LoadFileButton.setDisable(false);
        this.modeSwitch.setDisable(false);
    }

    public void ChangeYaz(int yaz)
    {
        currYazTimeLabel.setText(valueOf(yaz));
    }

    public void changeToDarkMode(){
        headerScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        headerScrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        headerScrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        headerScrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    public void setToggleOnEnable(){
        this.modeSwitch.setDisable(false);
    }


    @FXML
    void loadFileActionListenerClient(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", file.getName(), RequestBody.create(file, MediaType.parse("text/xml")))
                        .build();

        Request request = new Request.Builder()
                .url(FILE_UPLOAD)
                .post(body)
                .build();


        Call call = HTTP_CLIENT.newCall(request);
        String rawBody;
        Response response = call.execute();
       // try (Response response = call.execute()) {
        if (!response.isSuccessful()){
            rawBody  = response.body().string();
            getError(rawBody);
        }
        mainController.setBodyTabs();
    }

    @Override
    public void close() throws IOException {
        customersListView.getItems().clear();
        totalUsers.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    public void setHelloLabel(String name){
        helloLabel.setText("Welcome " + name);
    }
    private void updateUsersList(List<String> usersNames) {
        Platform.runLater(() -> {
            items = customersListView.getItems();
            items.clear();
            items.addAll(usersNames);
            totalUsers.set(usersNames.size());
            if(items!=null){
                customersListView.setItems(items);}
        });

    }

    private void updateYazTime(Integer yazTime){
        Platform.runLater(() -> {
            ChangeYaz(yazTime);
        });
    }

    public void startYazTimeRefresher() {
        currYazRefresher = new CurrYazRefresher(
                autoUpdate,
                this::updateYazTime);
        timer = new Timer();
        timer.schedule(currYazRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    public void startListRefresher() {
        listRefresher = new CustomersListRefresher(
                autoUpdate,
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }

    public void changeIfRewind(Boolean bool){
        LoadFileButton.setDisable(bool);
        if(bool){
            rewindMode.setText("You are in read only mode");
        }
        else {
            rewindMode.setText("Your account is active");
        }
    }

}
