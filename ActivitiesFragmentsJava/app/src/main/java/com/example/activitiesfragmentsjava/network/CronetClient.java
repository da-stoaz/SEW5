package com.example.activitiesfragmentsjava.network;

import android.content.Context;

import org.chromium.net.CronetEngine;

public final class CronetClient {


    private static volatile CronetEngine engine;

    private CronetClient() {
    }

    public static CronetEngine getInstance(Context context) {
        if (engine == null) {
            synchronized (CronetClient.class) {
                if (engine == null) {
                    engine = buildEngine(context.getApplicationContext());
                }
            }
        }
        return engine;
    }

    private static CronetEngine buildEngine(Context context) {
        return new CronetEngine.Builder(context)
                .enableHttp2(true)
                .enableQuic(true)
                .enableBrotli(true)
                .setUserAgent("ActivitiesFragmentsJava/1.0")
                .build();
    }
}
