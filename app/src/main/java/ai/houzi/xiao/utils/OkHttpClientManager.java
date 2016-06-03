package ai.houzi.xiao.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class OkHttpClientManager {
    public static OkHttpClientManager manager;
    private OkHttpClient client;
    private static Handler handler;

    private OkHttpClientManager() {
        if (client == null) {
            client = new OkHttpClient();
        }
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance() {
        if (manager == null) {
            synchronized (OkHttpClientManager.class) {
                manager = new OkHttpClientManager();
            }
        }
        return manager;
    }

    public static void postAsyn(String urlString, Map<String, String> maps, final ResultCallback callback) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        String url = urlString + "?";
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
            url += (entry.getKey() + "=" + entry.getValue() + "&");
        }
        Logg.e(url.substring(0, url.length() - 1));
        RequestBody body = builder.build();
        final Request request = new Request.Builder().post(body).url(urlString).build();
        callback.onBefore(request);
        getInstance().client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Request request, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            callback.onResponse(result);
                        } else {
                            callback.onException(response.message());
                        }
                        callback.onAfter();
                    }
                });
            }
        });
    }

    public static void getAsyn(String urlString, Map<String, String> maps, final ResultCallback callback) {
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            i++;
            if (i == 1) {
                urlString += "?";
            } else {
                urlString += "&";
            }
            urlString += entry.getKey() + "=" + entry.getValue();
        }
        Logg.e(urlString);
        final Request request = new Request.Builder().get().url(urlString).build();
        callback.onBefore(request);
        getInstance().client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Request request, final IOException e) {
                Logg.e(e.getMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            callback.onResponse(result);
                        } else {
                            callback.onException(response.message());
                            Logg.e(response.message());
                        }
                        callback.onAfter();
                    }
                });
            }
        });
    }

    public static abstract class ResultCallback {
        public void onBefore(Request request) {
        }

        public void onAfter() {
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(String result);

        public abstract void onException(String exception);
    }
}