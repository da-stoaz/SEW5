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

    private static final String BASE_URL = "http://10.0.2.2:5055/devices";
    private final CronetEngine cronetEngine;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DeviceApiService(Context context) {
        cronetEngine = new CronetEngine.Builder(context).build();
    }

    public void getAllDevices(Callback<List<DeviceData>> callback) {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                BASE_URL,
                new SimpleRequestCallback() {
                    @Override
                    public void onCompleted(String responseBody) {
                        try {
                            List<DeviceData> devices = parseDevices(responseBody);
                            mainHandler.post(() -> callback.onSuccess(devices));
                        } catch (JSONException e) {
                            mainHandler.post(() -> callback.onError(e));
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {
                        mainHandler.post(() -> callback.onError(e));
                    }
                },
                executor
        );
        requestBuilder.disableCache(); // Ensure we get fresh data
        requestBuilder.addHeader("Accept", "application/json");
        requestBuilder.build().start();
    }

    public void createDevice(DeviceData device, Callback<Void> callback) {
        try {
            String json = deviceToJson(device);
            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                    BASE_URL,
                    new SimpleRequestCallback() {
                        @Override
                        public void onCompleted(String responseBody) {
                            Log.d("DeviceApiService", "Create response: " + responseBody);
                            mainHandler.post(() -> callback.onSuccess(null));
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e("DeviceApiService", "Create device failed", e);
                            mainHandler.post(() -> callback.onError(e));
                        }
                    },
                    executor
            );
            requestBuilder.setHttpMethod("POST");
            requestBuilder.disableCache();
            requestBuilder.addHeader("Content-Type", "application/json");
            requestBuilder.addHeader("Accept", "application/json");
            requestBuilder.setUploadDataProvider(
                    UploadDataProviders.create(json.getBytes(StandardCharsets.UTF_8)),
                    executor
            );
            requestBuilder.build().start();
        } catch (JSONException e) {
            callback.onError(e);
        }
    }

    public void updateDevice(String id, DeviceData device, Callback<Void> callback) {
        try {
            String json = deviceToJson(device);
            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                    BASE_URL + "/" + id,
                    new SimpleRequestCallback() {
                        @Override
                        public void onCompleted(String responseBody) {
                            mainHandler.post(() -> callback.onSuccess(null));
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e("DeviceApiService", "Update device failed", e);
                            mainHandler.post(() -> callback.onError(e));
                        }
                    },
                    executor
            );
            requestBuilder.setHttpMethod("PUT");
            requestBuilder.disableCache();
            requestBuilder.addHeader("Content-Type", "application/json");
            requestBuilder.addHeader("Accept", "application/json");
            requestBuilder.setUploadDataProvider(
                    UploadDataProviders.create(json.getBytes(StandardCharsets.UTF_8)),
                    executor
            );
            requestBuilder.build().start();
        } catch (JSONException e) {
            callback.onError(e);
        }
    }

    public void deleteDevice(String id, Callback<Void> callback) {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                BASE_URL + "/" + id,
                new SimpleRequestCallback() {
                    @Override
                    public void onCompleted(String responseBody) {
                        mainHandler.post(() -> callback.onSuccess(null));
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("DeviceApiService", "Delete device failed", e);
                        mainHandler.post(() -> callback.onError(e));
                    }
                },
                executor
        );
        requestBuilder.setHttpMethod("DELETE");
        requestBuilder.disableCache();
        requestBuilder.addHeader("Accept", "application/json");
        requestBuilder.build().start();
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

    public interface Callback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }
}
