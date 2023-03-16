package components.body;

import dtos.DTOLoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import loans.status.StatusENUM;
import java.io.IOException;


public class loansInfoController {

    @FXML private TableView<DTOLoan> table;
    @FXML private AnchorPane anchorPane;
    @FXML private TableColumn<DTOLoan, String> IdLoan;
    @FXML private TableColumn<DTOLoan, String> loanOwner;
    @FXML private TableColumn<DTOLoan, String> category;
    @FXML private TableColumn<DTOLoan, Integer> loanAmount;
    @FXML private TableColumn<DTOLoan, Integer> totalYazTime;
    @FXML private TableColumn<DTOLoan, Integer> paysEveryYaz;
    @FXML private TableColumn<DTOLoan, Integer> interest;
    @FXML private TableColumn<DTOLoan, StatusENUM> loanStatus;
    @FXML private TableColumn<DTOLoan, Integer> totalFund;
    @FXML private TableColumn<DTOLoan, Integer> startActiveYaz;
    @FXML private TableColumn<DTOLoan, Integer> endingYaz;
    private BodyControllerAdmin bodyController;

    @FXML
    public void initialize(){
        IdLoan.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("IdLoan"));
        loanOwner.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("loanOwner"));
        category.setCellValueFactory(new PropertyValueFactory<DTOLoan, String>("category"));
        loanAmount.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("loanAmount"));
        totalYazTime.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalYazTime"));
        paysEveryYaz.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("paysEveryYaz"));
        startActiveYaz.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("startActiveYaz"));
        endingYaz.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("endingYaz"));
        interest.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("interest"));
        loanStatus.setCellValueFactory(new PropertyValueFactory<DTOLoan, StatusENUM>("loanStatus"));
        totalFund.setCellValueFactory(new PropertyValueFactory<DTOLoan, Integer>("totalFund"));
    }

    public void setBodyController(BodyControllerAdmin bodyController){
        this.bodyController = bodyController;
    }

    public void setLoanList(ObservableList<DTOLoan> lst) throws IOException {
        table.getItems().clear();
        ObservableList<DTOLoan> list = FXCollections.observableList(lst);
        table.setItems(list);

    }

    public void changeToDarkMode(){
        anchorPane.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        anchorPane.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        anchorPane.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        anchorPane.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
    }




}
