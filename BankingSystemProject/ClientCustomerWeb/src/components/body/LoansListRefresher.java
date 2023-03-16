package components.body;

import dtos.DTOLoan;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.appControllerClientCustomer.getError;
import static utils.Constants.GSON_INSTANCE;


public class LoansListRefresher extends TimerTask {

    private final Consumer<List<DTOLoan>> loansListConsumer;
    private final BooleanProperty shouldUpdate;
    private final String URLstr;


    public LoansListRefresher(BooleanProperty shouldUpdate, Consumer<List<DTOLoan>> loansListConsumer, String url) {
        this.shouldUpdate = shouldUpdate;
        this.loansListConsumer = loansListConsumer;
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

        HttpClientUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in getting loans info.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    DTOLoan[] loans = GSON_INSTANCE.fromJson(rawBody, DTOLoan[].class);
                    loansListConsumer.accept(Arrays.asList(loans));
                }
            }
        });
    }
}
