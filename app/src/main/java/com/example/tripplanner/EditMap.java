package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditMap extends AppCompatActivity {
    Button finishPlanningButton;
    Button addNewEventButton;
    Button editEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_map_activity);

        finishPlanningButton = findViewById(R.id.finishPlanningButton);
        addNewEventButton = findViewById(R.id.addNewEventButton);
        editEventButton = findViewById(R.id.editEventButton);

        finishPlanningButton.setOnClickListener(view -> {
            Intent finishEditingActivity = new Intent(this, TripDetails.class);
            this.startActivity(finishEditingActivity);
        });
        addNewEventButton.setOnClickListener(view -> {
            Intent addEventActivity = new Intent(this, NewEvent.class);
            this.startActivity(addEventActivity);
        });
        editEventButton.setOnClickListener(view -> {
            Intent editEventActivity = new Intent(this, EditEvent.class);
            this.startActivity(editEventActivity);
        });
    }


}
