package com.example.activitiesfragmentsjava.network;

import android.content.Context;
import android.net.Uri;

import com.example.activitiesfragmentsjava.R;

public class ApiConfig {

    private final String baseUrl;

    public ApiConfig(Context context) {
        String rawBaseUrl = context.getString(R.string.api_base_url).trim();
        if (!rawBaseUrl.endsWith("/")) {
            rawBaseUrl = rawBaseUrl + "/";
        }
        this.baseUrl = rawBaseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Uri getBaseUri() {
        return Uri.parse(baseUrl);
    }

    public String getDevicesUrl() {
        return baseUrl + "devices";
    }

    public String getDeviceUrl(String id) {
        return getDevicesUrl() + "/" + id;
    }
}
