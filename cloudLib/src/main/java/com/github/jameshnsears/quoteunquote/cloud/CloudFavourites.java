package com.github.jameshnsears.quoteunquote.cloud;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public final class CloudFavourites {
    public static final int TIMEOUT_SECONDS = 10;
    private static final String DNS = "8.8.8.8";
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void shutdown() {
        executorService.shutdown();
    }

    public boolean save(@NonNull final String payload) {
        final String endpointSave = BuildConfig.REMOTE_DEVICE_ENDPOINT + "/save";
        Timber.d("endpointSave=%s; payload=%s", endpointSave, payload);

        final Future<Boolean> future = executorService.submit(() -> {

            Request request = new Request.Builder()
                    .url(endpointSave)
                    .post(RequestBody.create(
                            payload,
                            MediaType.get("application/json; charset=utf-8")))
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();

            try {
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        Timber.d(request.toString());
                        return false;
                    }

                    Timber.d("response.body=%s", response.body().string());
                    return true;
                }
            } catch (SocketTimeoutException e) {
                return false;
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Timber.w(e.toString());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @NonNull
    public List<String> receive(final int timeout, @NonNull final String payload) {
        final String endpointLoad = BuildConfig.REMOTE_DEVICE_ENDPOINT + "/receive";
        Timber.d("endpointLoad=%s; payload=%s", endpointLoad, payload);

        final Request request = new Request.Builder()
                .url(endpointLoad)
                .post(RequestBody.create(
                        payload,
                        MediaType.get("application/json; charset=utf-8")))
                .build();

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();

        class CallbackFuture extends CompletableFuture<Response> implements Callback {
            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) {
                super.complete(response);

                final Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Timber.d("header=" + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
            }

            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException ioException) {
                super.completeExceptionally(ioException);
                Timber.e(ioException.toString());
            }
        }

        Response response = null;
        try {
            final CallbackFuture future = new CallbackFuture();
            client.newCall(request).enqueue(future);

            response = future.get();
            final String responseBody = escapeResponse(response.body().string());
            Timber.d("response.body=%s", responseBody);

            final Gson gson = new Gson();
            return gson.fromJson(responseBody, new TypeToken<List<String>>() {
            }.getType());
        } catch (ExecutionException | InterruptedException | IOException e) {
            Timber.w(e.toString());
            Thread.currentThread().interrupt();
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return new ArrayList<>();
    }

    @NonNull
    private String escapeResponse(@NonNull final String response) {
        String escapedResponse = response.replaceAll("\"", "");
        escapedResponse = escapedResponse.replaceAll("\\\\", "'");
        return escapedResponse;
    }

    public boolean isInternetAvailable() {
        final Future<Boolean> future = executorService.submit(() -> {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(DNS, 53), 1500);
                socket.close();

                Timber.d("isInternetAvailable=true");
                return true;
            } catch (IOException e) {
                Timber.d("isInternetAvailable=false");
                return false;
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Timber.w(e.toString());
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
