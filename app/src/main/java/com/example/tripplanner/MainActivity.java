package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity  implements Serializable {
    MyApp myapp;
    Button createTripButton;
    Button mytripsButton;
    Button ArButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myapp = new MyApp(this);

        createTripButton = findViewById(R.id.createTripButton);
        mytripsButton = findViewById(R.id.mytripsButton);
        ArButton = findViewById(R.id.ArButton);

        createTripButton.setOnClickListener(view -> {
            Intent createNewActivity = new Intent(this, NewTrip.class);
            this.startActivity(createNewActivity);
        });

        mytripsButton.setOnClickListener(view -> {
            Intent myTripsActivity = new Intent(this, MyTrips.class);
            this.startActivity(myTripsActivity);
        });
        ArButton.setOnClickListener(view -> {
            Intent myTripsActivity = new Intent(this, AR.class);
            this.startActivity(myTripsActivity);
        });
    }
}