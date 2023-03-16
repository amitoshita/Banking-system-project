package components.body;

import dtos.DTOLoan;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.appControllerClientCustomer.getError;
import static utils.Constants.GSON_INSTANCE;
import static utils.Constants.IS_REWIND;

public class IsRewindRefresher extends TimerTask {

    private final Consumer<Boolean> rewindConsumer;
    private final BooleanProperty shouldUpdate;


    public IsRewindRefresher(BooleanProperty shouldUpdate, Consumer<Boolean> rewindConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.rewindConsumer = rewindConsumer;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        String isReindURL = HttpUrl
                .parse(IS_REWIND)
                .newBuilder()
                .addQueryParameter("user-type", "customer")
                .build()
                .toString();

        HttpClientUtils.runAsync(isReindURL, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in rewind check.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    Boolean rewind = GSON_INSTANCE.fromJson(rawBody, Boolean.class);
                    rewindConsumer.accept(rewind);
                }
            }
        });
    }
}
