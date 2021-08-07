package com.example.tripplanner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyApp extends Application {
    public String orderId;
    SharedPreferences sharedPref;

    public MyApp(Context context){
        this.orderId="";
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        loadOrderId();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void loadOrderId() {
        this.orderId= sharedPref.getString("order", "");
    }

    public void saveOrderId(String id) {
        this.orderId=id;
        sharedPref.edit().putString("order", this.orderId).apply();
    }

}
