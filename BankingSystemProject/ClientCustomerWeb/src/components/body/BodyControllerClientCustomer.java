package components.body;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import components.app.appControllerClientCustomer;

import dtos.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import loans.Loan;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;
import java.io.PipedReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

import static utils.Constants.*;

public class BodyControllerClientCustomer {

    private appControllerClientCustomer mainController = null;
    private informationControllerClientCustomer informationCustomerController = null;
    private scrumbleControllerClientCustomer scrumbleCustomerController = null;
    private paymentControllerClientCustomer paymentCustomerController = null;
    private createLoanControllerClientCustomer createLoanClientCustomerController = null;
    private sellLoanControllerClientCustomer sellLoanControllerClientCustomer = null;
    private buyLoanControllerClientCustomer buyLoanControllerClientCustomer = null;
    private ScrollPane InformationCustomerTab;
    @FXML private GridPane inforamtionGrid;
    private ScrollPane ScrumbleCustomerTab;
    @FXML private GridPane ScrumbleTab;
    @FXML private GridPane CreateLoanTab;
    private ScrollPane PaymentCustomerTab;
    private ScrollPane createLoanCustomerTab;
    private ScrollPane sellLoanCustomerTab;
    private ScrollPane buyLoanCustomerTab;
    @FXML private GridPane PaymentTab;
    @FXML private GridPane SellLoanTab;
    @FXML private GridPane BuyLoanTab;
    @FXML private Tab informationTabID;
    @FXML private TabPane tabPane;
    @FXML private ScrollPane scrollPane;
    private LoansListRefresher loansListRefresher;
    private CustomerRefresher customerRefresher;
    private Timer timer;
    private final BooleanProperty autoUpdate = new SimpleBooleanProperty();
    private DTOcustomer customer;
    private List<DTOLoan> allLoans;


    public void setMainController(appControllerClientCustomer mainController){
        this.mainController = mainController;
        autoUpdate.bind(mainController.getAutoUpdateFromHeader());

    }

    public void setInformationTab()  throws IOException {
        if(informationCustomerController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(INFORMATION_CUSTOMER_ON_TAB);
            fxmlLoader.setLocation(url);
            InformationCustomerTab = fxmlLoader.load(url.openStream());
            InformationCustomerTab.setFitToWidth(true);
            InformationCustomerTab.setFitToHeight(true);
            informationCustomerController = fxmlLoader.getController();
            informationCustomerController.setMainController(this);
            if (isItsDarkMode()) {
                informationCustomerController.changeToDarkMode();
            }
        }
        inforamtionGrid.getChildren().setAll(InformationCustomerTab);
    }


    public DTOcustomer getCustomer() {
        return this.customer;
    }


    private void updateCustomerInfo(DTOcustomer updatedCustomer) {
        Platform.runLater(() -> {
            this.customer = updatedCustomer;
        });
    }

    public void startCustomerRefresher() {
        customerRefresher = new CustomerRefresher(
                autoUpdate,
                this::updateCustomerInfo,
                CUSTOMER);
        timer = new Timer();
        timer.schedule(customerRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateLoansListInfo(List<DTOLoan> updatedLoans) {
        Platform.runLater(() -> {
            this.allLoans = updatedLoans;
        });
    }

    public void startLoansListRefresher() {
        loansListRefresher = new LoansListRefresher(
                autoUpdate,
                this::updateLoansListInfo,
                LOANS_LIST);
        timer = new Timer();
        timer.schedule(loansListRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    public void setPaymentTab()  throws IOException {

        if(paymentCustomerController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(PAYMENT_CUSTOMER_ON_TAB);
            fxmlLoader.setLocation(url);
            PaymentCustomerTab = fxmlLoader.load(url.openStream());
            paymentCustomerController = fxmlLoader.getController();
            paymentCustomerController.setMainController(this);
            if (isItsDarkMode()) {
                paymentCustomerController.changeToDarkMode();
            }
        }
        /*paymentCustomerController.setLendersActiveRiskLoanList(mainController.filterLoansByNameStatus());*/
        PaymentTab.getChildren().setAll(PaymentCustomerTab);
        paymentCustomerController.startNotificationsRefresher();


    }

    public void setSellLoanTab()  throws IOException {

        if(sellLoanControllerClientCustomer == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(SELL_LOAN_ON_TAB);
            fxmlLoader.setLocation(url);
            sellLoanCustomerTab = fxmlLoader.load(url.openStream());
            sellLoanControllerClientCustomer = fxmlLoader.getController();
            sellLoanControllerClientCustomer.setMainController(this);
            if (isItsDarkMode()) {
                sellLoanControllerClientCustomer.changeToDarkMode();
            }
        }
        SellLoanTab.getChildren().setAll(sellLoanCustomerTab);
        ObservableList<DTOlender> items = getLendersLoanParts();
        sellLoanControllerClientCustomer.setLoansList(items);
    }

    public ObservableList<DTOlender> getLendersLoanParts(){
        ObservableList<DTOlender> items = FXCollections.observableArrayList();
        if(customer != null) {
            for (DTOLoan l : customer.getLenders().getDTOloanList()) {
                for (DTOlender d : l.getLenders()) {
                    if ((d.getLenderName().equals(customer.getName())) && (!d.isForSell())) {
                        items.add(d);
                    }
                }
            }
            return items;
        }
        else {
            return null;
        }
    }



    public void setSellTabControllerToNull(){
        sellLoanControllerClientCustomer = null;
    }

    public void setBuyControllerToNull(){
        buyLoanControllerClientCustomer = null;
    }
    public void setBuyLoanTab()  throws IOException {

        if(buyLoanControllerClientCustomer == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(BUY_LOAN_ON_TAB);
            fxmlLoader.setLocation(url);
            buyLoanCustomerTab = fxmlLoader.load(url.openStream());
            buyLoanControllerClientCustomer = fxmlLoader.getController();
            buyLoanControllerClientCustomer.setMainController(this);
            if (isItsDarkMode()) {
                buyLoanControllerClientCustomer.changeToDarkMode();
            }
        }
        BuyLoanTab.getChildren().setAll(buyLoanCustomerTab);

    }

    public ObservableList<DTOlender> getLoansForBuy(){
        ObservableList<DTOlender> items = FXCollections.observableArrayList();
        if(allLoans != null && customer != null) {
            for (DTOLoan l : allLoans){
                for(DTOlender d : l.getLenders()){
                    if ((!d.getLenderName().equals(customer.getName())) && (!l.getLoanOwner().equals(customer.getName()))
                    && d.isForSell()){
                        items.add(d);
                    }
                }
            }
            return items;
        }
        else
            return null;
    }

    private void updateLoansPartToBuy(List<DTOLoan> loanList) {
        Platform.runLater(() -> {
            boolean toRefresh = true;
            ObservableList<DTOlender> items = getLoansForBuy();
            try {
                if(buyLoanControllerClientCustomer.getLoansPartList() != null) {
                    for (DTOlender d : buyLoanControllerClientCustomer.getLoansPartList()) {
                        if ((d.getCheckProperty())) {
                            toRefresh = false;
                        }
                    }
                }
                if(toRefresh) {
                    buyLoanControllerClientCustomer.setLendersList(items);
                    buyLoanControllerClientCustomer.setEditable();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startLoansPartToBuyRefresher() {
        loansListRefresher = new LoansListRefresher(
                autoUpdate,
                this::updateLoansPartToBuy,
                LOANS_LIST);
        timer = new Timer();
        timer.schedule(loansListRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private void updateMyLoansOnRiskAndActiveOnPaymentTab(List<DTOLoan> loanList) {
        Platform.runLater(() -> {
            boolean toRefresh = true;
            ObservableList<DTOLoan> items = FXCollections.observableArrayList();
            items.addAll(loanList);
            items.removeIf(l -> (!(l.getLoanStatus().getName().equals("risk"))) && !(l.getLoanStatus().getName().equals("active")));
            try {
                if(paymentCustomerController.getLendersList() != null) {
                    for (DTOLoan l : paymentCustomerController.getLendersList()) {
                        if ((l.getCheckProperty())) {
                            toRefresh = false;
                        }
                    }
                }
                if(toRefresh) {
                    paymentCustomerController.setLendersActiveRiskLoanList(items);
                    paymentCustomerController.setEditable();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public BooleanProperty getAutoUpdate(){
        return mainController.getAutoUpdateFromHeader();
    }

    public void startMyLoansOnRiskAndActiveOnPaymentTabRefresher() {
        loansListRefresher = new LoansListRefresher(
                autoUpdate,
                this::updateMyLoansOnRiskAndActiveOnPaymentTab,
                MY_LOANS_LIST);
        timer = new Timer();
        timer.schedule(loansListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setScrumbleTab() throws IOException {
        if(scrumbleCustomerController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(SCRUMBLE_CUSTOMER_ON_TAB);
            fxmlLoader.setLocation(url);
            ScrumbleCustomerTab = fxmlLoader.load(url.openStream());
            scrumbleCustomerController = fxmlLoader.getController();
            scrumbleCustomerController.setMainController(this);
            if (isItsDarkMode()) {
                scrumbleCustomerController.changeToDarkMode();
            }
        }
        ScrumbleTab.getChildren().setAll(ScrumbleCustomerTab);
    }

    public void setCreateLoanClientCustomer() throws IOException {
        if(createLoanClientCustomerController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(CREATE_LOAN_CUSTOMER_ON_TAB);
            fxmlLoader.setLocation(url);
            createLoanCustomerTab = fxmlLoader.load(url.openStream());
            createLoanClientCustomerController = fxmlLoader.getController();
            createLoanClientCustomerController.setMainController(this);
            if (isItsDarkMode()) {
                createLoanClientCustomerController.changeToDarkMode();
            }
        }
        CreateLoanTab.getChildren().setAll(createLoanCustomerTab);
    }

    private void updateMyLoansList(List<DTOLoan> loanList) {
        Platform.runLater(() -> {
            ObservableList<DTOLoan> items = FXCollections.observableArrayList();
            items.addAll(loanList);
            try {
                informationCustomerController.setLoanersLoanList(items);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startMyLoansListRefresher() {
        loansListRefresher = new LoansListRefresher(
                autoUpdate,
                this::updateMyLoansList,
                MY_LOANS_LIST);
        timer = new Timer();
        timer.schedule(loansListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateMyInvestedLoansList(List<DTOLoan> loanList) {
        Platform.runLater(() -> {
            ObservableList<DTOLoan> items = FXCollections.observableArrayList();
            items.addAll(loanList);
            try {
                informationCustomerController.setLendersLoanList(items);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startMyInvestedLoansRefresher() {
        loansListRefresher = new LoansListRefresher(
                autoUpdate,
                this::updateMyInvestedLoansList,
                MY_INVESTED_LOANS_LIST);
        timer = new Timer();
        timer.schedule(loansListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateBankActionsList(DTOcustomer customer){
        Platform.runLater(() -> {
            informationCustomerController.setCustomerBankActions(customer.getListActions());
            boolean toRefresh = true;
            try {
                if(sellLoanControllerClientCustomer != null) {
                    for (DTOlender d : sellLoanControllerClientCustomer.getLoanList()) {
                        if (d.getCheckProperty()) {
                            toRefresh = false;
                        }
                    }
                }
                if(toRefresh){
                    setSellLoanTab();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startBankActionRefresher() {
        customerRefresher = new CustomerRefresher(
                autoUpdate,
                this::updateBankActionsList,
                CUSTOMER);
        timer = new Timer();
        timer.schedule(customerRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateCustomerBankBalance(DTOcustomer customer){
        Platform.runLater(() -> {
            informationCustomerController.setCustomerBalance(customer.getBalance());
        });
    }

    public void startBankCustomerBalanceRefresher() {
        customerRefresher = new CustomerRefresher(
                autoUpdate,
                this::updateCustomerBankBalance,
                CUSTOMER);
        timer = new Timer();
        timer.schedule(customerRefresher, REFRESH_RATE, REFRESH_RATE);
    }




    /*public void addMoneyToCustomer(String money){
        mainController.addMoneyToCurrCustomerByTransport(money);
    }*/

    /*public void minusMoneyToCustomer(String money){
        mainController.minusMoneyToCurrCustomerByTransport(money);
    }*/

    /*public ObservableList<DTOLoan> getFilterList(ObservableList<String> CategoriesList, int minimumInterest, int minimumYaz, int maximumLoans){

        return (mainController.filterLoansScrumble(CategoriesList, minimumInterest,minimumYaz, maximumLoans));
    }*/

    /*public int getCustomerBalance() {
        return mainController.getCustomer().getBalance();
    }*/

    public void setScrumbleTabControllerToNull(){
        scrumbleCustomerController = null;
    }

    public void setCreateLoanClientCustomerControllerToNull(){
        createLoanClientCustomerController = null;
    }

    /*public boolean conveyMoneyToLenders (DTOLoan loan)
    {
        return(mainController.payOnePulseByLoan(loan));
    }*/

    /*public boolean conveyAllMoneyToLenders(DTOLoan loan){
        return(mainController.payAllLoan(loan));
    }*/

    /*public void updateAdminTablesToAppController() throws IOException {
        mainController.updateAdminTablesBodyController();
    }*/


    public void changeToDarkMode()  {
        scrollPane.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        if(informationCustomerController!=null){
            informationCustomerController.changeToDarkMode();
        }
        if(paymentCustomerController!=null){
            paymentCustomerController.changeToDarkMode();
        }
        if(scrumbleCustomerController!=null){
            scrumbleCustomerController.changeToDarkMode();
        }
        if(createLoanClientCustomerController!=null){
            createLoanClientCustomerController.changeToDarkMode();
        }
        if(buyLoanControllerClientCustomer != null){
            buyLoanControllerClientCustomer.changeToDarkMode();
        }
        if(sellLoanControllerClientCustomer != null){
            sellLoanControllerClientCustomer.changeToDarkMode();
        }
    }


    public void changeToLightMode()  {
        scrollPane.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
        if(informationCustomerController!=null) {
            informationCustomerController.changeToLightMode();
        }
        if(paymentCustomerController!=null) {
            paymentCustomerController.changeToLightMode();
        }
        if(scrumbleCustomerController!=null) {
            scrumbleCustomerController.changeToLightMode();
        }
        if(createLoanClientCustomerController!=null) {
            createLoanClientCustomerController.changeToLightMode();
        }
        if(buyLoanControllerClientCustomer != null){
            buyLoanControllerClientCustomer.changeToLightMode();
        }
        if(sellLoanControllerClientCustomer != null){
            sellLoanControllerClientCustomer.changeToLightMode();
        }
    }

    public boolean isItsDarkMode(){
        return mainController.isIfItsDarkMode();
    }

    /*public boolean conveyMoneyToRiskLoanCustomerTab(int amount, DTOLoan loan){
        return mainController.conveyMoneyToRiskLoanAppController(amount, loan);
    }

    public DTOcustomer getCustomerByNameCustomerTab(){
        return mainController.getCustomer();
    }*/

    public void changeIfRewind(Boolean bool) {
        buyLoanControllerClientCustomer.changeIfRewind(bool);
        informationCustomerController.changeIfRewind(bool);
        paymentCustomerController.changeIfRewind(bool);
        scrumbleCustomerController.changeIfRewind(bool);
        sellLoanControllerClientCustomer.changeIfRewind(bool);
        createLoanClientCustomerController.changeIfRewind(bool);
    }
}

