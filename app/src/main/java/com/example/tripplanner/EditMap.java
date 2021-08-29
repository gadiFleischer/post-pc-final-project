package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

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
        this.myTrip = (TripModel) getTripIntent.getSerializableExtra("newTrip");


        finishPlanningButton.setOnClickListener(view -> {
            Intent finishEditingActivity = new Intent(this, TripDetails.class);
            this.startActivity(finishEditingActivity);
        });
        addNewEventButton.setOnClickListener(view -> {
            this.myApp.curAdress="keren hayesod 15";//get from marker
            Intent addEventActivity = new Intent(this, NewEvent.class);
            addEventActivity.putExtra("newTrip",this.myTrip );
            this.startActivity(addEventActivity);
        });
        editEventButton.setOnClickListener(view -> {
            Intent editEventActivity = new Intent(this, EditEvent.class);
            this.startActivity(editEventActivity);
        });
    }


}
