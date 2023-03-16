package utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.function.Consumer;

public class HttpClientAdminUtils {

    private final static SimpleCookieAdminManager simpleCookieManager = new SimpleCookieAdminManager();
    public final static OkHttpClient HTTP_ADMIN_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientAdminUtils.HTTP_ADMIN_CLIENT.newCall(request);

        call.enqueue(callback);
    }


    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_ADMIN_CLIENT.dispatcher().executorService().shutdown();
        HTTP_ADMIN_CLIENT.connectionPool().evictAll();
    }
}