package com.example.activitiesfragmentsjava.network;

import android.content.Context;
import android.net.Uri;

import org.chromium.net.CronetEngine;

public final class CronetClient {


    private static volatile CronetEngine engine;

    private CronetClient() {
    }

    public static CronetEngine getInstance(Context context, ApiConfig config) {
        if (engine == null) {
            synchronized (CronetClient.class) {
                if (engine == null) {
                    engine = buildEngine(context.getApplicationContext(), config);
                }
            }
        }
        return engine;
    }

    private static CronetEngine buildEngine(Context context, ApiConfig config) {
        CronetEngine.Builder builder = new CronetEngine.Builder(context)
                .enableHttp2(true)
                .enableQuic(true)
                .enableBrotli(true)
                .setUserAgent("ActivitiesFragmentsJava/1.0");

        addQuicHint(builder, config.getBaseUri());
        return builder.build();
    }

    private static void addQuicHint(CronetEngine.Builder builder, Uri baseUri) {
        String host = baseUri.getHost();
        if (host == null) {
            return;
        }
        int port = baseUri.getPort();
        if (port == -1) {
            port = "https".equalsIgnoreCase(baseUri.getScheme()) ? 443 : 80;
        }
        builder.addQuicHint(host, port, port);
    }
}
