package components.header;

import components.app.appControllerAdmin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.ToggleSwitch;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.String.valueOf;


public class headerControllerAdmin {


    private appControllerAdmin mainController;
    @FXML private Label currYazTimeLabel;
    @FXML private ToggleSwitch modeSwitch;
    @FXML private ScrollPane headerScrollPane;
    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;
    @FXML private ListView<String> customersListView = new ListView<>();
    private ObservableList<String> items;
    @FXML private ToggleButton autoUpdatesButton;
    @FXML private Button minusButtonRewind;
    @FXML private Button plusButtonRewind;
    @FXML private ToggleSwitch rewindSwitch;
    private Integer yazInRewind = 1;
    private Integer currYaz = 1;
    private Boolean isRewind = false;

    @FXML
    void minusButtonOnAction(ActionEvent event) {
        if(yazInRewind>1){
            yazInRewind--;
        }
        if (yazInRewind == 1){
            minusButtonRewind.setDisable(true);
        }
        if (yazInRewind != currYaz){
            plusButtonRewind.setDisable(false);
        }

        ChangeYaz(yazInRewind);
        mainController.setLoansTableListByYaz(yazInRewind);
        mainController.setCustomersTableListByYaz(yazInRewind);
    }

    @FXML
    void plusButtonOnAction(ActionEvent event) {

        if(yazInRewind<currYaz){
            yazInRewind++;
        }
        if (yazInRewind == currYaz)
        {
            plusButtonRewind.setDisable(true);
        }
        if (yazInRewind != 1){
            minusButtonRewind.setDisable(false);
        }
        ChangeYaz(yazInRewind);
        mainController.setLoansTableListByYaz(yazInRewind);
        mainController.setCustomersTableListByYaz(yazInRewind);
    }

    public Integer getYazInRewind() {
        return yazInRewind;
    }

    public int getCurrYaz() {
        return currYaz;
    }

    public void setCurrYaz(Integer currYaz) {
        this.currYaz = currYaz;
    }

    public Boolean getRewind() {
        return isRewind;
    }

    public headerControllerAdmin() {
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

        rewindSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                mainController.setRewindMode(true);
                yazInRewind = currYaz;
                isRewind= true;

            } else {
                mainController.setRewindMode(false);
                ChangeYaz(currYaz);
                isRewind=false;
            }
        });
        autoUpdate.bind(autoUpdatesButton.selectedProperty());
    }

    public void setPlusAndMinus(){
        if(minusButtonRewind.isDisable() && plusButtonRewind.isDisable()){
            minusButtonRewind.setDisable(false);
            plusButtonRewind.setDisable(false);
        }
        else if (!minusButtonRewind.isDisable() && !plusButtonRewind.isDisable()){
            minusButtonRewind.setDisable(true);
            plusButtonRewind.setDisable(true);
        }
    }


    public void setMainController(appControllerAdmin mainController){
        this.mainController = mainController;
    }

    public void setButtonsToEnable(){
        this.modeSwitch.setDisable(false);
    }

    public void ChangeYaz(int yaz)
    {
        currYazTimeLabel.setText(valueOf(yaz));
        if(!isRewind){
            currYaz = yaz;
        }

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

    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }

}
