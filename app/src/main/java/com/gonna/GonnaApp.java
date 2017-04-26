package com.gonna;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by Maxim on 4/3/2017.
 */

public class GonnaApp extends Application {
    @Override public void onCreate() {
        super.onCreate();

        //FirebaseApp.initializeApp(this);
    }
}
