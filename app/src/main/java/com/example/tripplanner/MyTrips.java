package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyTrips extends AppCompatActivity {
    Button tripExampleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trips_activty);

        tripExampleButton = findViewById(R.id.tripExampleButton);

        tripExampleButton.setOnClickListener(view -> {
            Intent tripDetailsActivity = new Intent(this, TripDetails.class);
            this.startActivity(tripDetailsActivity);
        });

    }
}
