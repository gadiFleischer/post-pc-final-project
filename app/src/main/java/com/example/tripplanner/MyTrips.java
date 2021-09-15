package com.example.tripplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.models.TripModel;
import com.hbb20.CountryPickerView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyTrips extends AppCompatActivity implements Serializable {

    public TripItemsHolder holder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trips_activty);
        MyApp myApp;
        myApp = new MyApp(this);

        if (holder == null) {
            holder = new TripItemsHolder();
        }
        holder.setItems(myApp.myTrips);
        TextView noTripsMsg = findViewById(R.id.noTripsMsg);
        if(myApp.myTrips.size()==0){
            noTripsMsg.setText("You have no trips yet");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerTrips);
        TripItemAdapter recyclerAdapter = new TripItemAdapter(holder, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
}

