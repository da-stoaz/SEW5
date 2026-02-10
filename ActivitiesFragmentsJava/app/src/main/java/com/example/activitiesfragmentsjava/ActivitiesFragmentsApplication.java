package com.example.activitiesfragmentsjava;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.net.CronetProviderInstaller;

public class ActivitiesFragmentsApplication extends Application {

    private static final String TAG = "ActivitiesFragmentsApp";

    @Override
    public void onCreate() {
        super.onCreate();
        CronetProviderInstaller.installProvider(this)
                .addOnSuccessListener(unused -> Log.d(TAG, "Cronet provider installed"))
                .addOnFailureListener(e -> Log.w(TAG, "Cronet provider install failed, using fallback", e));
    }
}
