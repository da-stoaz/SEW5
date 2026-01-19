package com.example.activitiesfragmentsjava.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.activitiesfragmentsjava.data.DeviceData;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProviders;
import org.chromium.net.UrlRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceApiService {

    private static final String TAG = "DeviceApiService";
    private static final ExecutorService NETWORK_EXECUTOR = Executors.newSingleThreadExecutor();

    private final ApiConfig apiConfig;
    private final CronetEngine cronetEngine;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DeviceApiService(Context context) {
        Context appContext = context.getApplicationContext();
        apiConfig = new ApiConfig(appContext);
        cronetEngine = CronetClient.getInstance(appContext, apiConfig);
    }

    public void getAllDevices(Callback<List<DeviceData>> callback) {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                apiConfig.getDevicesUrl(),
                new SimpleRequestCallback() {
                    @Override
                    public void onCompleted(String responseBody) {
                        try {
                            List<DeviceData> devices = parseDevices(responseBody);
                            postSuccess(callback, devices);
                        } catch (JSONException e) {
                            postError(callback, e);
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {
                        postError(callback, e);
                    }
                },
                NETWORK_EXECUTOR
        );
        configureJsonRequest(requestBuilder, true, UrlRequest.Builder.REQUEST_PRIORITY_MEDIUM);
        requestBuilder.build().start();
    }

    public void createDevice(DeviceData device, Callback<Void> callback) {
        try {
            String json = deviceToJson(device);
            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                    apiConfig.getDevicesUrl(),
                    new SimpleRequestCallback() {
                        @Override
                        public void onCompleted(String responseBody) {
                            Log.d(TAG, "Create response: " + responseBody);
                            postSuccess(callback, null);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e(TAG, "Create device failed", e);
                            postError(callback, e);
                        }
                    },
                    NETWORK_EXECUTOR
            );
            requestBuilder.setHttpMethod("POST");
            configureJsonRequest(requestBuilder, false, UrlRequest.Builder.REQUEST_PRIORITY_HIGHEST);
            requestBuilder.addHeader("Content-Type", "application/json");
            requestBuilder.setUploadDataProvider(
                    UploadDataProviders.create(json.getBytes(StandardCharsets.UTF_8)),
                    NETWORK_EXECUTOR
            );
            requestBuilder.build().start();
        } catch (JSONException e) {
            postError(callback, e);
        }
    }

    public void updateDevice(String id, DeviceData device, Callback<Void> callback) {
        try {
            String json = deviceToJson(device);
            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                    apiConfig.getDeviceUrl(id),
                    new SimpleRequestCallback() {
                        @Override
                        public void onCompleted(String responseBody) {
                            postSuccess(callback, null);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e(TAG, "Update device failed", e);
                            postError(callback, e);
                        }
                    },
                    NETWORK_EXECUTOR
            );
            requestBuilder.setHttpMethod("PUT");
            configureJsonRequest(requestBuilder, false, UrlRequest.Builder.REQUEST_PRIORITY_HIGHEST);
            requestBuilder.addHeader("Content-Type", "application/json");
            requestBuilder.setUploadDataProvider(
                    UploadDataProviders.create(json.getBytes(StandardCharsets.UTF_8)),
                    NETWORK_EXECUTOR
            );
            requestBuilder.build().start();
        } catch (JSONException e) {
            postError(callback, e);
        }
    }

    public void deleteDevice(String id, Callback<Void> callback) {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                apiConfig.getDeviceUrl(id),
                new SimpleRequestCallback() {
                    @Override
                    public void onCompleted(String responseBody) {
                        postSuccess(callback, null);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "Delete device failed", e);
                        postError(callback, e);
                    }
                },
                NETWORK_EXECUTOR
        );
        requestBuilder.setHttpMethod("DELETE");
        configureJsonRequest(requestBuilder, false, UrlRequest.Builder.REQUEST_PRIORITY_HIGHEST);
        requestBuilder.build().start();
    }

    private void configureJsonRequest(UrlRequest.Builder builder, boolean allowCache, int priority) {
        builder.addHeader("Accept", "application/json");
        builder.setPriority(priority);
        if (!allowCache) {
            builder.disableCache();
        }
    }

    private List<DeviceData> parseDevices(String json) throws JSONException {
        List<DeviceData> devices = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String name = obj.optString("deviceName");
            String manufacturer = obj.optString("manufacturer");
            String serialNumber = obj.optString("serialNumber");
            String description = obj.optString("description");
            String id = obj.optString("id");

            DeviceData device = new DeviceData(name, manufacturer, serialNumber, description);
            device.setId(id);
            devices.add(device);
        }
        return devices;
    }

    private String deviceToJson(DeviceData device) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("deviceName", device.getDeviceName());
        obj.put("manufacturer", device.getManufacturer());
        obj.put("serialNumber", device.getSerialNumber());
        obj.put("description", device.getDescription());
        return obj.toString();
    }

    private <T> void postSuccess(Callback<T> callback, T result) {
        // Cronet callbacks run off the main thread, so marshal UI updates safely.
        mainHandler.post(() -> callback.onSuccess(result));
    }

    private void postError(Callback<?> callback, Exception e) {
        mainHandler.post(() -> callback.onError(e));
    }

    public interface Callback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }
}
