package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMapActivity extends AppCompatActivity {
    Button editEventMarkerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map_activity);

        editEventMarkerButton = findViewById(R.id.editEventMarkerButton);

        editEventMarkerButton.setOnClickListener(view -> {
            Intent editEventMarkerButtonActivity = new Intent(this, EditEvent.class);
            this.startActivity(editEventMarkerButtonActivity);
        });
    }
}
