package com.example.activitiesfragmentsjava.network;

import android.content.Context;

import com.example.activitiesfragmentsjava.R;


//API Base URL in api_config.xml konfigurieren. Localhost funktioniert nicht mit Android Emulator.
public class ApiConfig {

    private final String devicesUrl;

    public ApiConfig(Context context) {
        String baseUrl = context.getString(R.string.api_base_url).trim();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        devicesUrl = baseUrl + "devices";
    }

    public String getDevicesUrl() {
        return devicesUrl;
    }

    public String getDeviceUrl(String id) {
        return devicesUrl + "/" + id;
    }
}
