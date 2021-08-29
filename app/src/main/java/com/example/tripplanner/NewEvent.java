package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.TripModel;

import java.util.Date;

public class NewEvent extends AppCompatActivity {
    Button addNewEventButton;
    TripModel myTrip;
    MyApp myApp;
    String address="";
    String nickName="";
    CategoryEvent category=CategoryEvent.OTHER;
    int day=0;
    Date startTime;
    Date EndTime;
    String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event_activity);
        myApp = new MyApp(this);
        addNewEventButton = findViewById(R.id.addNewEventButton);
        Intent getTripIntent=getIntent();
        this.myTrip = (TripModel) getTripIntent.getSerializableExtra("newTrip");
        this.address=myApp.curAdress;

        addNewEventButton.setOnClickListener(view -> {
            Intent editMapActivity = new Intent(this, EditMap.class);
            this.startActivity(editMapActivity);
        });

    }
}
