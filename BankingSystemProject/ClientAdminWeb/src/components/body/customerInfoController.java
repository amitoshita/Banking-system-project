package components.body;
import components.app.appControllerAdmin;
import dtos.DTOLoan;
import dtos.DTOcustomer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientAdminUtils;


import java.io.IOException;
import java.util.Objects;

import static components.app.appControllerAdmin.getErrorJSON;
import static utils.Constants.GSON_INSTANCE;

public class customerInfoController {

    @FXML private TableView<DTOcustomer> table;
    @FXML private TableColumn<DTOcustomer, String> name;
    @FXML private TableColumn<DTOcustomer, Integer> balance;
    @FXML private AnchorPane anchorPane;
    private TableRowExpanderColumn<DTOcustomer> moreInfo;
    private ObservableList<DTOcustomer> list;
    private BodyControllerAdmin bodyController;
    private DTOcustomer customer;
    private boolean expendedParam = false;

    @FXML
    public void initialize() {

        table.setEditable(true);
        name.setCellValueFactory(new PropertyValueFactory<DTOcustomer, String>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<DTOcustomer, Integer>("balance"));
        moreInfo = new TableRowExpanderColumn<>(this::buildBorrowersExpender);
        moreInfo.setText("More info");
        moreInfo.setPrefWidth(250);
        //lenders.setText("Lenders");
        table.getColumns().add(moreInfo);
        //table.getColumns().add(lenders);
    }

    public void setBodyController(BodyControllerAdmin bodyController) {
        this.bodyController = bodyController;
    }

    public void setCustomersList(ObservableList<DTOcustomer> lst) throws IOException {
        list = FXCollections.observableList(lst);
        table.setItems(list);

    }


    public HBox buildBorrowersExpender(TableRowExpanderColumn.TableRowDataFeatures<DTOcustomer> param) {
        this.expendedParam = param.isExpanded();
        param.getTableRow().setAlignment(Pos.CENTER);
        HBox base = new HBox(40);
        VBox editorBorrowers = new VBox(10);
        VBox editorLenders = new VBox(10);
        Text PendingAmount = new Text();
        PendingAmount.setFill(Color.AQUAMARINE.darker().darker());
        PendingAmount.setText("Pending loans: " + getLoansAmountBorrowers("pending", param.getValue()));
        Text ActiveAmount = new Text();
        ActiveAmount.setFill(Color.AQUAMARINE.darker().darker());
        ActiveAmount.setText("Active loans: " + getLoansAmountBorrowers("active", param.getValue()));
        Text RiskAmount = new Text();
        RiskAmount.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount.setText("Risk loans: " + getLoansAmountBorrowers("risk", param.getValue()));
        Text FinishAmount = new Text();
        FinishAmount.setText("Finished loans: " + getLoansAmountBorrowers("finished", param.getValue()));
        Text PendingAmount1 = new Text();
        PendingAmount1.setFill(Color.AQUAMARINE.darker().darker());
        PendingAmount1.setText("Pending loans: " + getLoansAmountLenders("pending", param.getValue()));
        Text ActiveAmount1 = new Text();
        ActiveAmount1.setFill(Color.AQUAMARINE.darker().darker());
        ActiveAmount1.setText("Active loans: " + getLoansAmountLenders("active", param.getValue()));
        Text RiskAmount1 = new Text();
        FinishAmount.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount1.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount1.setText("Risk loans: " + getLoansAmountLenders("risk", param.getValue()));
        Text FinishAmount1 = new Text();
        FinishAmount1.setFill(Color.AQUAMARINE.darker().darker());
        FinishAmount1.setText("Finished loans: " + getLoansAmountLenders("finished", param.getValue()));

        Label borrowersLabel = new Label("My loans:");
        Label lendersLabel = new Label("My investments:");
        borrowersLabel.setStyle("-fx-font-weight: bold");
        lendersLabel.setStyle("-fx-font-weight: bold");

        editorLenders.getChildren().addAll(lendersLabel, PendingAmount1, ActiveAmount1, RiskAmount1, FinishAmount1);
        editorBorrowers.getChildren().addAll(borrowersLabel, PendingAmount, ActiveAmount, RiskAmount, FinishAmount);

        base.getChildren().addAll(editorBorrowers, editorLenders);
        base.setAlignment(Pos.TOP_LEFT);
        return base;

    }

    public boolean getIfParamExpended() {
        return expendedParam;
    }

    public int getLoansAmountBorrowers(String status, DTOcustomer user){
        int count = 0;
        for(DTOLoan l : user.getBorrowers().getDTOloanList()){
            if (l.getLoanStatusString().equals(status)){
                count++;
            }
        }
        return count;
    }

    public int getLoansAmountLenders(String status, DTOcustomer user){
        int count = 0;
        for(DTOLoan l : user.getLenders().getDTOloanList()){
            if (l.getLoanStatusString().equals(status)){
                count++;
            }
        }
        return count;
    }
    /*public VBox buildLendersExpender(TableRowExpanderColumn.TableRowDataFeatures<DTOcustomer> param){
        VBox editor = new VBox(10);
        Text PendingAmount = new Text();
        PendingAmount.setFill(Color.AQUAMARINE.darker().darker());
        PendingAmount.setText("Pending loans: " + bodyController.getLoansAmountFromAppControllerLenders("pending", param.getValue().getName()));
        Text ActiveAmount = new Text();
        ActiveAmount.setFill(Color.AQUAMARINE.darker().darker());
        ActiveAmount.setText("Active loans: " + bodyController.getLoansAmountFromAppControllerLenders("active", param.getValue().getName()));
        Text RiskAmount = new Text();
        RiskAmount.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount.setText("Risk loans: " + bodyController.getLoansAmountFromAppControllerLenders("risk", param.getValue().getName()));
        Text FinishAmount = new Text();
        FinishAmount.setFill(Color.AQUAMARINE.darker().darker());
        FinishAmount.setText("Finished loans: " + bodyController.getLoansAmountFromAppControllerLenders("finished", param.getValue().getName()));
        editor.getChildren().addAll(PendingAmount, ActiveAmount, RiskAmount, FinishAmount);
        editor.setAlignment(Pos.TOP_LEFT);
        return editor;
    }*/

    public void changeToDarkMode(){
        anchorPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        anchorPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        anchorPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        anchorPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }


}


