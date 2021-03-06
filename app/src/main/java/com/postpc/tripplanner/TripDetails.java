package com.postpc.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import com.postpc.tripplanner.R;
import com.postpc.tripplanner.models.EventModel;
import com.postpc.tripplanner.models.TripModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Intent getTripIntent = getIntent();
        myTrip = myApp.getTripById(getTripIntent.getStringExtra("tripId"));

        expandableListView = findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(myTrip);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        Collections.sort(expandableListTitle, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.split("\\s+")[1];
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String name = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
            String[] splitted=name.split(" \\| At: ");
            EventModel event = myTrip.getEventByName(splitted[0]);
            Intent showOnMapButtonActivity = new Intent(this, EditMapActivity.class);
            showOnMapButtonActivity.putExtra("tripId", this.myTrip.id);
            showOnMapButtonActivity.putExtra("lat", event.position.latitude);
            showOnMapButtonActivity.putExtra("long", event.position.longitude);
            this.startActivity(showOnMapButtonActivity);
            finish();
            return false;

        });

        finishPlanningButton.setOnClickListener(view -> {
            Intent MyTripsActivity = new Intent(this, MyTrips.class);
            this.startActivity(MyTripsActivity);
            finish();
        });

        showOnMapButton.setOnClickListener(view -> {
            Intent showOnMapButtonActivity = new Intent(this, EditMapActivity.class);
            showOnMapButtonActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(showOnMapButtonActivity);
            finish();
        });
    }



}
