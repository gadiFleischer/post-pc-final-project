package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TripDetails extends AppCompatActivity {
    Button showOnMapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details_activity);

        showOnMapButton = findViewById(R.id.showOnMapButton);

        showOnMapButton.setOnClickListener(view -> {
            Intent showOnMapButtonActivity = new Intent(this, MainMapActivity.class);
            this.startActivity(showOnMapButtonActivity);
        });
    }
}
