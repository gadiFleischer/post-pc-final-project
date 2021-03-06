package com.postpc.tripplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postpc.tripplanner.R;
import com.postpc.tripplanner.models.TripModel;
import com.hbb20.CountryPickerView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NewTrip extends AppCompatActivity implements Serializable {
    Button startPlanningButton;
    EditText tripTitleEdit;
    EditText startDateEdit;
    EditText endDateEdit;
    Date startDate;
    Date endDate;
    CountryPickerView ccp;
    String countryName="";
    String countryCode="";
    String title="";
    final Calendar myCalendar = Calendar.getInstance();
    MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip_activity);

        startPlanningButton = findViewById(R.id.startPlanningButton);
        tripTitleEdit = findViewById(R.id.editTripTitleEdit);
        startDateEdit = findViewById(R.id.editStartDateEdit);
        endDateEdit = findViewById(R.id.editEndDateEdit);
        ccp=findViewById(R.id.editCountry);
        myApp = new MyApp(this);


        startPlanningButton.setOnClickListener(view -> {
            if(this.ccp.getCpViewHelper().getSelectedCountry().getValue()==null){
                Toast toast = Toast.makeText(this,"You did not Pick a country", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            this.countryName = Objects.requireNonNull(this.ccp.getCpViewHelper().getSelectedCountry().getValue()).getEnglishName();
            this.countryCode = this.ccp.getCpViewHelper().getSelectedCountry().getValue().getFlagEmoji();
            this.title =tripTitleEdit.getText().toString();
            if(this.title.equals("") || this.countryName.equals("") ||startDate==null||endDate==null){
                Toast toast = Toast.makeText(this,"One or more fields are empty", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if(endDate.compareTo(startDate)<0){
                Toast toast = Toast.makeText(this,"Start date is after end date", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            TripModel newTrip = new TripModel(title, countryName, startDate, endDate, countryCode);
            try {
                newTrip.initDaysArrayAndPicker();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.myApp.myTrips.add(newTrip);
            this.myApp.saveMyTrips();

            Intent tripDetailsActivity = new Intent(this, TripDetails.class);
            tripDetailsActivity.putExtra("tripId", newTrip.id);
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
            new DatePickerDialog(NewTrip.this, startDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        endDateEdit.setOnClickListener(v -> {
            new DatePickerDialog(NewTrip.this, endDateListener, myCalendar
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