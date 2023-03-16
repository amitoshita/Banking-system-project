package body.admin;

import body.BodyController;
import dtos.*;
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
import org.controlsfx.control.table.TableRowExpanderColumn;


import java.io.IOException;

public class customerInfoController {

    @FXML private TableView<DTOcustomer> table;
    @FXML private TableColumn<DTOcustomer, String> name;
    @FXML private TableColumn<DTOcustomer, Integer> balance;
    @FXML private AnchorPane anchorPane;
    private TableRowExpanderColumn<DTOcustomer> moreInfo;
   // private TableRowExpanderColumn<DTOcustomer> lenders;
    private ObservableList<DTOcustomer> list;
    private BodyController bodyController;
    @FXML
    public void initialize(){

        table.setEditable(true);
        name.setCellValueFactory(new PropertyValueFactory<DTOcustomer, String>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<DTOcustomer, Integer>("balance"));
        moreInfo = new TableRowExpanderColumn<>(this::buildBorrowersExpender);
        //lenders = new TableRowExpanderColumn<>(this::buildLendersExpender);
        moreInfo.setText("More info");
        moreInfo.setPrefWidth(250);
        //lenders.setText("Lenders");
        table.getColumns().add(moreInfo);
        //table.getColumns().add(lenders);
    }

    public void setBodyController(BodyController bodyController){
        this.bodyController = bodyController;
    }
    public void setCustomersList(ObservableList<DTOcustomer> lst) throws IOException {
        ObservableList<DTOcustomer> list = FXCollections.observableList(lst);
        table.setItems(list);

    }

    public HBox buildBorrowersExpender(TableRowExpanderColumn.TableRowDataFeatures<DTOcustomer> param){
        HBox base = new HBox(40);
        VBox editorBorrowers = new VBox(10);
        VBox editorLenders = new VBox(10);
        Text PendingAmount = new Text();
        PendingAmount.setFill(Color.AQUAMARINE.darker().darker());
        PendingAmount.setText("Pending loans: " + bodyController.getLoansAmountFromAppControllerBorrowers("pending", param.getValue().getName()));
        Text ActiveAmount = new Text();
        ActiveAmount.setFill(Color.AQUAMARINE.darker().darker());
        ActiveAmount.setText("Active loans: " + bodyController.getLoansAmountFromAppControllerBorrowers("active", param.getValue().getName()));
        Text RiskAmount = new Text();
        RiskAmount.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount.setText("Risk loans: " + bodyController.getLoansAmountFromAppControllerBorrowers("risk", param.getValue().getName()));
        Text FinishAmount = new Text();
        FinishAmount.setText("Finished loans: " + bodyController.getLoansAmountFromAppControllerBorrowers("finished", param.getValue().getName()));
        Text PendingAmount1 = new Text();
        PendingAmount1.setFill(Color.AQUAMARINE.darker().darker());
        PendingAmount1.setText("Pending loans: " + bodyController.getLoansAmountFromAppControllerLenders("pending", param.getValue().getName()));
        Text ActiveAmount1 = new Text();
        ActiveAmount1.setFill(Color.AQUAMARINE.darker().darker());
        ActiveAmount1.setText("Active loans: " + bodyController.getLoansAmountFromAppControllerLenders("active", param.getValue().getName()));
        Text RiskAmount1 = new Text();
        FinishAmount.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount1.setFill(Color.AQUAMARINE.darker().darker());
        RiskAmount1.setText("Risk loans: " + bodyController.getLoansAmountFromAppControllerLenders("risk", param.getValue().getName()));
        Text FinishAmount1 = new Text();
        FinishAmount1.setFill(Color.AQUAMARINE.darker().darker());
        FinishAmount1.setText("Finished loans: " + bodyController.getLoansAmountFromAppControllerLenders("finished", param.getValue().getName()));

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
        anchorPane.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        anchorPane.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        anchorPane.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        anchorPane.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
    }


}


