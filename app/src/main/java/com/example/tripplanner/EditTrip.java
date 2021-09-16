package com.example.tripplanner;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tripplanner.models.TripModel;
import com.hbb20.CountryPickerView;
import com.hbb20.countrypicker.models.CPCountry;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditTrip extends AppCompatActivity implements Serializable {
    Button finishEditingButton;
    EditText tripTitleEdit;
    EditText startDateEdit;
    EditText endDateEdit;
    TextView destination;
    Date startDate;
    Date endDate;
    final Calendar myCalendar = Calendar.getInstance();
    MyApp myApp;
    TripModel myTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_trip_activity);

        finishEditingButton = findViewById(R.id.finishEditingButton);
        tripTitleEdit = findViewById(R.id.editTripTitleEdit);
        startDateEdit = findViewById(R.id.editStartDateEdit);
        endDateEdit = findViewById(R.id.editEndDateEdit);
        destination = findViewById(R.id.editDestinationView);
        myApp = new MyApp(this);

        Intent getTripIntent=getIntent();
        myTrip=myApp.getTripById(getTripIntent.getStringExtra("tripId"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
        startDate=myTrip.startDate;
        endDate=myTrip.endDate;
        tripTitleEdit.setText(myTrip.title);
        startDateEdit.setText(sdf.format(myTrip.startDate));
        endDateEdit.setText(sdf.format((myTrip.endDate)));
        destination.setText("Destination: "+myTrip.destination);


        finishEditingButton.setOnClickListener(view -> {

            myTrip.title =tripTitleEdit.getText().toString();
            myTrip.startDate = startDate;
            myTrip.endDate = endDate;
            if(myTrip.title.equals("") || myTrip.destination.equals("") ||myTrip.startDate==null||myTrip.endDate==null||myTrip.endDate.compareTo(myTrip.startDate)<0){
                Toast toast = Toast.makeText(this,"your inputs are invalid", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            try {
                myTrip.setDaysArrayAndPicker();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.myApp.saveMyTrips();

            Intent tripDetailsActivity = new Intent(this, MyTrips.class);
            tripDetailsActivity.putExtra("tripId", myTrip.id);
            this.startActivity(tripDetailsActivity);
            finish();

        });


        DatePickerDialog.OnDateSetListener startDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(true);
            startDate = myCalendar.getTime();
        };
        DatePickerDialog.OnDateSetListener endDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(false);
            endDate = myCalendar.getTime();
        };

        startDateEdit.setOnClickListener(v -> {
            new DatePickerDialog(EditTrip.this, startDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        endDateEdit.setOnClickListener(v -> {
            new DatePickerDialog(EditTrip.this, endDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel(boolean isStart) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
        if(isStart){
            startDateEdit.setText(sdf.format(myCalendar.getTime()));
        }else{
            endDateEdit.setText(sdf.format(myCalendar.getTime()));
        }
    }
}