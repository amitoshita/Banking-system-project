package components.body;

import dtos.DTOallBankActions;
import dtos.DTOcustomer;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtils;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.appControllerClientCustomer.getError;
import static utils.Constants.GSON_INSTANCE;

public class CustomerRefresher extends TimerTask {

    private final Consumer<DTOcustomer> customerConsumer;
    private final BooleanProperty shouldUpdate;
    private final String URLstr;


    public CustomerRefresher(BooleanProperty shouldUpdate, Consumer<DTOcustomer> customerConsumer, String url) {
        this.shouldUpdate = shouldUpdate;
        this.customerConsumer = customerConsumer;
        this.URLstr = url;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(URLstr)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in getting loans info.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    DTOcustomer customer = GSON_INSTANCE.fromJson(rawBody, DTOcustomer.class);
                    customerConsumer.accept(customer);
                }
            }
        });
    }
}
