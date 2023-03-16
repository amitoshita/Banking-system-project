package utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static int REFRESH_RATE = 500;
    // fxml locations

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080" + "/web-banking_Web";

    public final static String LOGIN_PAGE = BASE_URL + "/loginShortResponse";
    public final static String USERS_LIST = BASE_URL + "/customersListNames";
    public final static String FILE_UPLOAD = BASE_URL + "/upload-file";
    public final static String MY_LOANS_LIST = BASE_URL + "/myLoans";
    public final static String MY_INVESTED_LOANS_LIST = BASE_URL + "/myInvestedLoans";
    public final static String CUSTOMER = BASE_URL + "/customer";
    public final static String UPDATE_MONEY = BASE_URL + "/update-money";
    public final static String CREATE_LOAN = BASE_URL + "/create-loan";
    public final static String SCRUMBLING = BASE_URL + "/scrumble";
    public final static String FILTERED_LIST = BASE_URL + "/filter-list";
    public final static String CATEGORIES = BASE_URL + "/categories";
    public final static String CONVEYMONEY = BASE_URL + "/convey-money";
    public static final String BUY_LOAN = BASE_URL + "/buy-loan";
    public static final String SELL_LOAN = BASE_URL + "/sell-loan";
    public final static String LOANS_LIST = BASE_URL + "/loansList";
    public final static String INCREASE_YAZ = BASE_URL + "/yazServlet";
    public final static String IS_REWIND = BASE_URL + "/is-Rewind";


    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public final static String INFORMATION_CUSTOMER_ON_TAB = "/components/body/informationClientCustomer.fxml";
    public final static String SCRUMBLE_CUSTOMER_ON_TAB = "/components/body/scrumbleClientCustomer.fxml";
    public final static String PAYMENT_CUSTOMER_ON_TAB = "/components/body/paymentClientCustomer.fxml";
    public static final String BODY_TAB_FXML = "/components/body/bodyClientCustomer.fxml";
    public final static String APP_FXML = "/components/app/appClientCustomer.fxml";
    public final static String CREATE_LOAN_CUSTOMER_ON_TAB = "/components/body/createLoanClientCustomer.fxml";
    public final static String SELL_LOAN_ON_TAB = "/components/body/sellLoanClientCustomer.fxml";
    public final static String BUY_LOAN_ON_TAB = "/components/body/buyLoanClientCustomer.fxml";

}
