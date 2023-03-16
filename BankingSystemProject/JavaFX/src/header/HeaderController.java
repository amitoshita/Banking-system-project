package header;

import app.AppController;
import dtos.DTOcustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.util.List;

import static java.lang.String.valueOf;

public class HeaderController {

    private AppController mainController;
    @FXML private Label loadedFileLabel;
    @FXML private Label yazLabel;
    @FXML private ComboBox viewByCB;
    @FXML private Label currYazTimeLabel;
    @FXML private ToggleSwitch modeSwitch;
    @FXML private ScrollPane headerScrollPane;



    @FXML
    public void initialize() {
        modeSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                mainController.changeToDarkMode();
            } else {
                    mainController.changeToLightMode();
            }
        });
    }

    public void setMainController(AppController mainController){
        this.mainController = mainController;
    }

    public void loadFileOnLabel(String s){
        loadedFileLabel.setText(s);
    }

    public void setComboBoxItems(List<DTOcustomer> dtoCustomers){
        viewByCB.getItems().add("Admin");
        for(DTOcustomer c : dtoCustomers) {
            viewByCB.getItems().add(c.getName());
        }
    }

    @FXML
    void switchScreen(ActionEvent event) throws IOException {
        if (viewByCB.getSelectionModel().getSelectedIndex() != 0){
            mainController.setCurrentUserName(String.valueOf(viewByCB.getValue()));
            mainController.switchScreenToCustomer();
        }
        else {
            mainController.setCurrentUserName(String.valueOf(viewByCB.getValue()));
            mainController.switchScreenToAdmin();
        }

    }

    public void ChangeYaz(int yaz)
    {
        currYazTimeLabel.setText(valueOf(yaz));
    }

    public void changeToDarkMode(){
        headerScrollPane.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        headerScrollPane.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        headerScrollPane.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        headerScrollPane.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
    }

    public void setToggleOnEnable(){
        this.modeSwitch.setDisable(false);
    }


}
