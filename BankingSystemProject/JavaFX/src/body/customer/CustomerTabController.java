package body.customer;

import dtos.DTOLoan;
import dtos.DTOcustomer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import app.AppController;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CustomerTabController {

        public final static String INFORMATION_CUSTOMER_ON_TAB = "/body/customer/InformationCustomerOnTab.fxml";
        public final static String SCRUMBLE_CUSTOMER_ON_TAB = "/body/customer/scrumbleCustomerOnTab.fxml";
        public final static String PAYMENT_CUSTOMER_ON_TAB = "/body/customer/PaymentCustomerOnTab.fxml";
        private AppController mainController = null;
        private InformationCustomerController informationCustomerController;
        private ScrumbleCustomerController scrumbleCustomerController = null;
        private PaymentCustomerController paymentCustomerController = null;
        private ScrollPane InformationCustomerTab;
        @FXML private GridPane inforamtionGrid;
        private ScrollPane ScrumbleCustomerTab;
        @FXML private GridPane ScrumbleTab;
        private ScrollPane PaymentCustomerTab;
        @FXML private GridPane PaymentTab;;
        @FXML private Tab informationTabID;
        @FXML private TabPane tabPane;
        @FXML private ScrollPane scrollPane;


        public void setMainController(AppController mainController){
                this.mainController = mainController;

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
                if (isItsDarkMode() == true) {
                    informationCustomerController.changeToDarkMode();
                }
            }
            informationCustomerController.setLendersLoanList(mainController.getLendersLoansByName());
            informationCustomerController.setLoanersLoanList(mainController.getLoanerLoansByName());
            inforamtionGrid.getChildren().setAll(InformationCustomerTab);
            informationCustomerController.setCustomerBalance(mainController.getCustomer().getBalance());
            informationCustomerController.setCustomerBankActions(mainController.getCustomer().getListActions());
        }



        public void setPaymentTab()  throws IOException {

            if(paymentCustomerController == null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource(PAYMENT_CUSTOMER_ON_TAB);
                fxmlLoader.setLocation(url);
                PaymentCustomerTab = fxmlLoader.load(url.openStream());
                paymentCustomerController = fxmlLoader.getController();
                paymentCustomerController.setMainController(this);
                if (isItsDarkMode() == true) {
                    paymentCustomerController.changeToDarkMode();
                }
            }
            paymentCustomerController.setLendersActiveRiskLoanList(mainController.filterLoansByNameStatus());
            PaymentTab.getChildren().setAll(PaymentCustomerTab);
            paymentCustomerController.updateNotificationOnPaymentTab();

        }

        public void setScrumbleTab() throws IOException {
            if(scrumbleCustomerController == null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource(SCRUMBLE_CUSTOMER_ON_TAB);
                fxmlLoader.setLocation(url);
                ScrumbleCustomerTab = fxmlLoader.load(url.openStream());
                scrumbleCustomerController = fxmlLoader.getController();
                scrumbleCustomerController.setMainController(this);
                if (isItsDarkMode() == true) {
                    scrumbleCustomerController.changeToDarkMode();
                }
            }
            ScrumbleTab.getChildren().setAll(ScrumbleCustomerTab);
        }

        public void addMoneyToCustomer(String money){
                mainController.addMoneyToCurrCustomerByTransport(money);
        }

        public void minusMoneyToCustomer(String money){
                mainController.minusMoneyToCurrCustomerByTransport(money);
        }

        public ObservableList<DTOLoan> getFilterList(ObservableList<String> CategoriesList, int minimumInterest,int minimumYaz, int maximumLoans){

                return (mainController.filterLoansScrumble(CategoriesList, minimumInterest,minimumYaz, maximumLoans));
        }

    public int getCustomerBalance() {
            return mainController.getCustomer().getBalance();
        }

        public void conveyTaskDataToAppController(List<DTOLoan> loanList, int amount, int precent) throws InterruptedException {
            mainController.scrumbleTasking(loanList, amount, precent);
        }
        public void setScrumbleTabControllerToNull(){
            scrumbleCustomerController = null;
        }

        public boolean conveyMoneyToLenders (DTOLoan loan)
        {
            return(mainController.payOnePulseByLoan(loan));
        }

    public boolean conveyAllMoneyToLenders(DTOLoan loan){
            return(mainController.payAllLoan(loan));
    }

    public void updateAdminTablesToAppController() throws IOException {
            mainController.updateAdminTablesBodyController();
    }


    public void changeToDarkMode()  {
        scrollPane.getStylesheets().removeAll(getClass().getResource("/app/appLightMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        if(informationCustomerController!=null){
            informationCustomerController.changeToDarkMode();
        }
        if(paymentCustomerController!=null){
            paymentCustomerController.changeToDarkMode();
        }
        if(scrumbleCustomerController!=null){
            scrumbleCustomerController.changeToDarkMode();
        }
    }


    public void changeToLightMode()  {
        scrollPane.getStylesheets().removeAll(getClass().getResource("/app/appDarkMode.css").toExternalForm());
        scrollPane.getStylesheets().add(getClass().getResource("/app/appLightMode.css").toExternalForm());
        if(informationCustomerController!=null) {
            informationCustomerController.changeToLightMode();
        }
        if(paymentCustomerController!=null) {
            paymentCustomerController.changeToLightMode();
        }
        if(scrumbleCustomerController!=null) {
            scrumbleCustomerController.changeToLightMode();
        }
    }

    public boolean isItsDarkMode(){
            return mainController.isIfItsDarkMode();
    }

    public boolean conveyMoneyToRiskLoanCustomerTab(int amount, DTOLoan loan){
            return mainController.conveyMoneyToRiskLoanAppController(amount, loan);
    }

    public DTOcustomer getCustomerByNameCustomerTab(){
            return mainController.getCustomer();
    }

}


