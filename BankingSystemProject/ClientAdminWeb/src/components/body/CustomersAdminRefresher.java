package components.body;

import com.google.gson.JsonSyntaxException;
import dtos.DTOcustomer;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientAdminUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.appControllerAdmin.getError;
import static utils.Constants.GSON_INSTANCE;

public class CustomersAdminRefresher extends TimerTask {

    private final Consumer<List<DTOcustomer>> customersConsumer;
    private final BooleanProperty shouldUpdate;
    private final String URLstr;


    public CustomersAdminRefresher(BooleanProperty shouldUpdate, Consumer<List<DTOcustomer>> customersConsumer, String url) {
        this.shouldUpdate = shouldUpdate;
        this.customersConsumer = customersConsumer;
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
                .addQueryParameter("rewind", "false")
                .build()
                .toString();

        HttpClientAdminUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in getting loans info.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    DTOcustomer[] customers = GSON_INSTANCE.fromJson(rawBody, DTOcustomer[].class);
                    customersConsumer.accept(Arrays.asList(customers));

                }
            }
        });
    }
}
