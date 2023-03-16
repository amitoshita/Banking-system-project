package utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static int REFRESH_RATE = 1000;
    // fxml locations

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080" + "/web-banking_Web";

    public final static String LOGIN_PAGE = BASE_URL + "/loginShortResponse";
    public final static String CUSTOMERS_LIST = BASE_URL + "/customersList";
    public final static String LOANS_LIST = BASE_URL + "/loansList";
    public final static String INCREASE_YAZ = BASE_URL + "/yazServlet";
    public final static String IS_REWIND = BASE_URL + "/is-Rewind";

    public final static String CUSTOMERS_INFO_FXML = "/components/body/CustomerFxml.fxml";
    public final static String LOANS_INFO_FXML = "/components/body/loansFxml.fxml";
    public final static String BODY_FXML = "/components/body/BodyAdmin.fxml";
    public final static String APP_FXML = "/components/app/appAdmin.fxml";
    public final static Gson GSON_INSTANCE = new Gson();

}
