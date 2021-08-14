package com.example.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MyApp myapp;
    Button createTripButton;
    Button mytripsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myapp = new MyApp(this);

        createTripButton = findViewById(R.id.createTripButton);
        mytripsButton = findViewById(R.id.mytripsButton);

        createTripButton.setOnClickListener(view -> {
            Intent createNewActivity = new Intent(this, NewTrip.class);
            this.startActivity(createNewActivity);
        });
    }
}