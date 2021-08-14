package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class NewTrip extends AppCompatActivity {
    Button startPlanningButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip_activity);

        startPlanningButton = findViewById(R.id.startPlanningButton);

        startPlanningButton.setOnClickListener(view -> {
            Intent editMapActivity = new Intent(this, EditMap.class);
            this.startActivity(editMapActivity);
        });
    }
}