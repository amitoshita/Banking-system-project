package components.body;

import exceptions.fileDivideYazPaymentException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import loans.Loan;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;

import static components.app.appControllerClientCustomer.getError;

public class createLoanControllerClientCustomer {

    private BodyControllerClientCustomer customerTabController;
    @FXML ScrollPane scrollPaneNewLoan;
    @FXML private TextField loanIDText;
    @FXML private TextField categoryText;
    @FXML private TextField amountText;
    @FXML private TextField totalYazText;
    @FXML private TextField paysEveryYazText;
    @FXML private TextField interestText;
    @FXML private Button createLoanButton;

    public void setMainController(BodyControllerClientCustomer customerTabController){
        this.customerTabController = customerTabController;
    }

    public void changeToDarkMode(){
        scrollPaneNewLoan.getStylesheets().removeAll(getClass().getResource("/components/appLightMode.css").toExternalForm());
        scrollPaneNewLoan.getStylesheets().add(getClass().getResource("/components/appDarkMode.css").toExternalForm());
    }

    public void changeToLightMode(){
        scrollPaneNewLoan.getStylesheets().removeAll(getClass().getResource("/components/appDarkMode.css").toExternalForm());
        scrollPaneNewLoan.getStylesheets().add(getClass().getResource("/components/appLightMode.css").toExternalForm());
    }

    @FXML
    void createLoan(ActionEvent event) {
        if (!loanIDText.getText().trim().isEmpty() && !categoryText.getText().trim().isEmpty() && !amountText.getText().trim().isEmpty()
            && !totalYazText.getText().trim().isEmpty() && !paysEveryYazText.getText().trim().isEmpty() && !interestText.getText().trim().isEmpty()){

            try {
                Integer.parseInt(amountText.getText().trim());
                int totalYaz = Integer.parseInt(totalYazText.getText().trim());
                int payEveryYaz = Integer.parseInt(paysEveryYazText.getText().trim());
                Integer.parseInt(interestText.getText().trim());
                if ((totalYaz % payEveryYaz) != 0){
                    throw new fileDivideYazPaymentException();
                }
                String createLoanURL = HttpUrl
                        .parse(Constants.CREATE_LOAN)
                        .newBuilder()
                        .addQueryParameter("loanID", loanIDText.getText().trim())
                        .addQueryParameter("category", categoryText.getText().trim())
                        .addQueryParameter("amount", amountText.getText().trim())
                        .addQueryParameter("totalYaz", totalYazText.getText().trim())
                        .addQueryParameter("paysEveryYaz", paysEveryYazText.getText().trim())
                        .addQueryParameter("interest", interestText.getText().trim())
                        .build()
                        .toString();

                HttpClientUtils.runAsync(createLoanURL, new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() ->
                                getError("Unable to create new loan.")
                        );
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {
                            String responseBody = response.body().string();
                            Platform.runLater(() ->
                                    getError(responseBody)

                            );
                        } else {
                            Platform.runLater(() -> {
                                scrumbleControllerClientCustomer.getDoneMessage();
                                try {
                                    customerTabController.setCreateLoanClientCustomerControllerToNull();
                                    customerTabController.setCreateLoanClientCustomer();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                });
            } catch (NumberFormatException e) {
                getError("You entered not a number. Exception: " + e);
            } catch (fileDivideYazPaymentException e) {
                getError("Exception: " + e);
            }
        }
    }

    public void changeIfRewind(Boolean bool){
        createLoanButton.setDisable(bool);
    }


}
