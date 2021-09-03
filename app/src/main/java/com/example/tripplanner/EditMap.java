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
import java.util.Date;

public class EditMap extends AppCompatActivity {
    Button finishPlanningButton;
    Button addNewEventButton;
    Button editEventButton;
    TripModel myTrip;
    MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_map_activity);
        finishPlanningButton = findViewById(R.id.finishPlanningButton);
        addNewEventButton = findViewById(R.id.addNewEventButton);
        editEventButton = findViewById(R.id.editEventButton);
        myApp = new MyApp(this);


        Intent getTripIntent=getIntent();
        String id= getTripIntent.getStringExtra("tripId");
        this.myTrip=myApp.getTripById(id);

        finishPlanningButton.setOnClickListener(view -> {
            Intent finishEditingActivity = new Intent(this, TripDetails.class);
            this.startActivity(finishEditingActivity);
        });
        addNewEventButton.setOnClickListener(view -> {
            String address = "keren hayesod 15";//TODO: get from marker
            LatLng pos= new LatLng(100,100);//TODO:get from marker
            EventModel newEvent = new EventModel("",address,CategoryEvent.OTHER,pos,1,"","","");
            Intent addEventActivity = new Intent(this, NewEvent.class);
            addEventActivity.putExtra("newEvent", (Serializable) newEvent);
            addEventActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(addEventActivity);
        });
        editEventButton.setOnClickListener(view -> {
            Intent editEventActivity = new Intent(this, EditEvent.class);
            this.startActivity(editEventActivity);
        });
    }


}
