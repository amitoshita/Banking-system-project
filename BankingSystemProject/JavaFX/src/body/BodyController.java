package body;
import app.AppController;
import body.admin.customerInfoController;
import body.admin.loansInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class BodyController {

    public final static String CUSTOMERS_INFO_FXML = "/body/admin/CustomerFxml.fxml";
    public final static String LOANS_INFO_FXML = "/body/admin/loansFxml.fxml";
    private AppController mainController;
    private loansInfoController loansInfoController;
    private customerInfoController customerInfoController;
    @FXML private Button loadFileButton;
    @FXML private Button IncreaseYazButton;
    @FXML private AnchorPane loanInfoOnAdmin;
    @FXML private ScrollPane loanInfoOnAdminScrollPane;
    @FXML private ScrollPane customersInfoOnAdminScrollPane;
    @FXML private AnchorPane customersInfoOnAdmin;
    @FXML private ScrollPane bodyScrollPane;

    public void setMainController(AppController mainController){
        this.mainController = mainController;
    }


    @FXML
    void loadFileActionListener(ActionEvent event){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                mainController.loadFileOnLabel(file.getAbsolutePath());
            }

    }


    public void setButtonsToEnable(){
        IncreaseYazButton.setDisable(false);
    }

    public void setButtonsToDisable(){
        IncreaseYazButton.setDisable(true);
    }

    @FXML
    public void addYazAdmin(ActionEvent event) throws IOException {
        mainController.changeCurrYaz();
    }


    public void showLoansInformation() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(LOANS_INFO_FXML);
        fxmlLoader.setLocation(url);
        AnchorPane TableLoansRoot = fxmlLoader.load(url.openStream());
        loansInfoController = fxmlLoader.getController();
        loansInfoController.setBodyController(this);
        loansInfoController.setLoanList(mainController.getDTOLoansInfo());
        loanInfoOnAdmin.getChildren().setAll(TableLoansRoot);
    }

    public void showCustomersInfo() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(CUSTOMERS_INFO_FXML);
        fxmlLoader.setLocation(url);
        AnchorPane TableCustomersRoot = fxmlLoader.load(url.openStream());
        customerInfoController = fxmlLoader.getController();
        customerInfoController.setBodyController(this);
        customerInfoController.setCustomersList(mainController.getDTOCustomerInfo());
        customersInfoOnAdmin.getChildren().setAll(TableCustomersRoot);
    }

    public int getLoansAmountFromAppControllerBorrowers(String status, String customer){
        return mainController.getLoansAmountFromTransportBorrowers(status, customer);
    }

    public int getLoansAmountFromAppControllerLenders(String status, String customer){
        return mainController.getLoansAmountFromTransportLenders(status, customer);
    }

    public void changeToDarkMode(){
        bodyScrollPane.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        bodyScrollPane.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        loansInfoController.changeToDarkMode();
        customerInfoController.changeToDarkMode();
    }

    public void changeToLightMode(){
        bodyScrollPane.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        bodyScrollPane.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
        loansInfoController.changeToLightMode();
        customerInfoController.changeToLightMode();
    }


}

