package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NewEvent extends AppCompatActivity {
    Button addNewEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event_activity);

        addNewEventButton = findViewById(R.id.addNewEventButton);

        addNewEventButton.setOnClickListener(view -> {
            Intent editMapActivity = new Intent(this, EditMap.class);
            this.startActivity(editMapActivity);
        });

    }
}
