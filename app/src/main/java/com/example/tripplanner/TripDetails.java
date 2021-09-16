package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;
import com.google.android.material.navigation.NavigationView;
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

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


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
        dl = findViewById(R.id.trip_details_activity);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv =  findViewById(R.id.nv);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.day:
                        //TODO: open new window
                        break;
                    case R.id.category:
                        //TODO: open new window
                        break;
                    default:
                        return true;
                }
                return true;

            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}
