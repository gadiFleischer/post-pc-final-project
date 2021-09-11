package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TripDetails extends AppCompatActivity {
    Button showOnMapButton;
    Button finishPlanningButton;
    TripModel myTrip;
    MyApp myApp;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details_activity);

        showOnMapButton = findViewById(R.id.showOnMapButton);
        finishPlanningButton = findViewById(R.id.finishPlanningButton);

        myApp = new MyApp(this);
        Intent getTripIntent=getIntent();
        myTrip=myApp.getTripById(getTripIntent.getStringExtra("tripId"));


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(myTrip);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        Collections.sort(expandableListTitle);
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
//        expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
//                expandableListTitle.get(groupPosition) + " List Expanded.",
//                Toast.LENGTH_SHORT).show());

//        expandableListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(),
//                expandableListTitle.get(groupPosition) + " List Collapsed.",
//                Toast.LENGTH_SHORT).show());

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String name = expandableListDetail.get( expandableListTitle.get(groupPosition)).get(childPosition);
            EventModel event = myTrip.getEventByName(name);
            Intent showOnMapButtonActivity = new Intent(this, EditMapActivity.class);
            showOnMapButtonActivity.putExtra("tripId", this.myTrip.id);
            showOnMapButtonActivity.putExtra("lat", event.position.latitude);
            showOnMapButtonActivity.putExtra("long", event.position.longitude);
            this.startActivity(showOnMapButtonActivity);
            return false;
        });


        finishPlanningButton.setOnClickListener(view -> {
            Intent MyTripsActivity = new Intent(this, MyTrips.class);
            this.startActivity(MyTripsActivity);
        });

        showOnMapButton.setOnClickListener(view -> {
            Intent showOnMapButtonActivity = new Intent(this, EditMapActivity.class);
            showOnMapButtonActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(showOnMapButtonActivity);
        });
    }
}
