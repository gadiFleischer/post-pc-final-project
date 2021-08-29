package com.example.tripplanner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.tripplanner.models.TripModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyApp extends Application {
    SharedPreferences sharedPref;
    public ArrayList<TripModel> myTrips;
    public String curAdress="";
    public MyApp(Context context){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        loadMyTrips();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void loadMyTrips() {
        myTrips= new ArrayList<>();
        String tripsJson = sharedPref.getString("myTrips", "");
        if (!tripsJson.equals("")) {
            Type listType = new TypeToken<ArrayList<TripModel>>(){}.getType();
            myTrips = new Gson().fromJson(tripsJson, listType);
        }
    }

    public void saveMyTrips(ArrayList<TripModel> trips) {
        myTrips=trips;
        String itemsJson = new Gson().toJson(myTrips);
        sharedPref.edit().putString("myTrips", itemsJson).apply();
    }
    public void saveMyTrips() {
        String itemsJson = new Gson().toJson(myTrips);
        sharedPref.edit().putString("myTrips", itemsJson).apply();
    }


}
