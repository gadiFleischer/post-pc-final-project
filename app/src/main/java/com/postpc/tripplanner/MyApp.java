package com.postpc.tripplanner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.postpc.tripplanner.models.CategoryEvent;
import com.postpc.tripplanner.models.EventModel;
import com.postpc.tripplanner.models.TripModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyApp extends Application {
    float[] colors = new float[]{
            15, 180, 255, 135, 240, 330, 210, 75, 270, 315, 90, 105, 45, 345, 120, 60, 165, 90, 30, 195,
            225, 285, 150, 300, 0,
    };

    String[] categoryItems = new String[]{"SIGHT", "FOOD", "HOTEL","OTHER"};

    SharedPreferences sharedPref;
    public ArrayList<TripModel> myTrips;
    public EventModel curTempEvent;
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

    public void loadTempEvent(){
        String tripsJson = sharedPref.getString("tempEvent", "");
        if (!tripsJson.equals("")) {
            Type listType = new TypeToken<EventModel>(){}.getType();
            curTempEvent = new Gson().fromJson(tripsJson, listType);
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

    public void saveTempEvent(){
        String itemsJson = new Gson().toJson(curTempEvent);
        sharedPref.edit().putString("tempEvent", itemsJson).apply();
    }
    public void saveTrip(TripModel toSaveTrip) {
        ArrayList<TripModel> trips = this.myTrips;
        for (int i = 0, tripsSize = trips.size(); i < tripsSize; i++) {
            if (trips.get(i).id.equals(toSaveTrip.id)) {
               this.myTrips.set(i,toSaveTrip);
            }
        }
        String itemsJson = new Gson().toJson(myTrips);
        sharedPref.edit().putString("myTrips", itemsJson).apply();
    }

    public CategoryEvent getCategoryFromString(String name){
        switch(name) {
            case "FOOD":
                return CategoryEvent.FOOD;
            case "HOTEL":
                return CategoryEvent.HOTEL;
            case "SIGHT":
                return CategoryEvent.SIGHT;
            default:
                return CategoryEvent.OTHER;
        }
    }

    public TripModel getTripById(String id){
        for (TripModel trip : this.myTrips)
        {
            if (trip.id.equals(id))
            {
                return trip;
            }
        }
        return null;
    }
    public static int getEventByIdIndex(TripModel trip, int day, String eventId){

        ArrayList<EventModel> events = trip.days.get(day).events;
        for (int i = 0, eventsSize = events.size(); i < eventsSize; i++) {
            EventModel event = events.get(i);
            if (event.id.equals(eventId)) {
                return i;
            }
        }
        return -1;
    }

    public void deleteTrip(int index){
        this.myTrips.remove(index);
        this.saveMyTrips();
    }
}
