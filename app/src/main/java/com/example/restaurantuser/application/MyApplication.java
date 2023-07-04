package com.example.restaurantuser.application;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("MyApplication", "FirebaseDatabase persistence enabled");

        // Pemanggilan setPersistenceEnabled di sini
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
