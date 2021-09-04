package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyTrips extends AppCompatActivity {

    public TripItemsHolder holder = null;


    Button tripExampleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trips_activty);

        if (holder == null) {
            holder = new TripItemsHolder();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerTodoItemsList);
        TripItemAdapter recyclerAdapter = new TripItemAdapter(holder, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);

        tripExampleButton = findViewById(R.id.tripExampleButton);

        tripExampleButton.setOnClickListener(view -> {
            Intent tripDetailsActivity = new Intent(this, TripDetails.class);
            this.startActivity(tripDetailsActivity);
        });

    }
}
