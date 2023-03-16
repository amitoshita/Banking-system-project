package components.body;

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
import static utils.Constants.INCREASE_YAZ;

public class CurrYazRefresher extends TimerTask {

    private final Consumer<Integer> currYazTime;
    private final BooleanProperty shouldUpdate;


    public CurrYazRefresher(BooleanProperty shouldUpdate, Consumer<Integer> yazConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.currYazTime = yazConsumer;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(INCREASE_YAZ)
                .newBuilder()
                .addQueryParameter("action", "update")
                .build()
                .toString();

        HttpClientUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getError("Error in increase yaz.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    Integer yaz = GSON_INSTANCE.fromJson(rawBody, Integer.class);
                    currYazTime.accept(yaz);
                }
            }
        });
    }
}
