package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditEvent extends AppCompatActivity {
    Button deleteEditButton;
    Button DoneEditButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activty);

        deleteEditButton = findViewById(R.id.deleteEditButton);
        DoneEditButton = findViewById(R.id.DoneEditButton);

        DoneEditButton.setOnClickListener(view -> {
            Intent editMapActivity = new Intent(this, EditMap.class);
            this.startActivity(editMapActivity);
        });

        deleteEditButton.setOnClickListener(view -> {
            Intent editMapActivity = new Intent(this, EditMap.class);
            this.startActivity(editMapActivity);
        });
    }
}
