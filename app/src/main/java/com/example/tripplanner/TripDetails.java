package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class TripDetails extends AppCompatActivity {
    Button showOnMapButton;
    Button finishPlanningButton;
    Button addNewEventButton;
    Button editEventButton;
    TripModel myTrip;
    MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details_activity);

        showOnMapButton = findViewById(R.id.showOnMapButton);
        finishPlanningButton = findViewById(R.id.finishPlanningButton);
//        addNewEventButton = findViewById(R.id.addNewEventButton);
//        editEventButton = findViewById(R.id.editEventButton);
        myApp = new MyApp(this);


        Intent getTripIntent=getIntent();
        String id= getTripIntent.getStringExtra("tripId");
        myTrip=myApp.getTripById(id);
        String x = "";

        finishPlanningButton.setOnClickListener(view -> {
            Intent MyTripsActivity = new Intent(this, MyTrips.class);
            this.startActivity(MyTripsActivity);
        });
//        addNewEventButton.setOnClickListener(view -> {
//            String address = "keren hayesod 15";//TODO: get from marker
//            LatLng pos= new LatLng(100,100);//TODO:get from marker
//            EventModel newEvent = new EventModel("",address, CategoryEvent.OTHER,pos,1,"","","");
//            Intent addEventActivity = new Intent(this, NewEvent.class);
//            addEventActivity.putExtra("newEvent", (Serializable) newEvent);
//            addEventActivity.putExtra("tripId", this.myTrip.id);
//            this.startActivity(addEventActivity);
//        });
//        editEventButton.setOnClickListener(view -> {
//            //TODO: get Id from marker and use Trip.getEventById()
//            String toEditEvent = this.myTrip.days.get(0).events.get(0).id;
//            Intent editEventActivity = new Intent(this, EditEvent.class);
//            editEventActivity.putExtra("EventId", toEditEvent);
//            editEventActivity.putExtra("tripId", this.myTrip.id);
//            this.startActivity(editEventActivity);
//        });

        showOnMapButton.setOnClickListener(view -> {
            Intent showOnMapButtonActivity = new Intent(this, EditMapActivity.class);
            showOnMapButtonActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(showOnMapButtonActivity);
        });
    }
}
