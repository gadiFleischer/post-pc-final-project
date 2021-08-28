package com.example.tripplanner;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripplanner.models.TripModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTrip extends AppCompatActivity {
    Button startPlanningButton;
    EditText tripTitleEdit;
    EditText destinationEdit;
    EditText startDateEdit;
    EditText endDateEdit;
    Date startDate;
    Date endDate;

    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip_activity);

        startPlanningButton = findViewById(R.id.startPlanningButton);
        tripTitleEdit = findViewById(R.id.tripTitleEdit);
        destinationEdit = findViewById(R.id.destinationEdit);
        startDateEdit = findViewById(R.id.startDateEdit);
        endDateEdit = findViewById(R.id.endDateEdit);

        startPlanningButton.setOnClickListener(view -> {
            TripModel newTrip = new TripModel(tripTitleEdit.getText().toString(), destinationEdit.getText().toString(), startDate, endDate, "");
            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("newTrip", newTrip);
            this.startActivity(editMapActivity);
        });

        DatePickerDialog.OnDateSetListener startDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(true);
        };
        DatePickerDialog.OnDateSetListener endDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(false);
        };

        startDateEdit.setOnClickListener(v -> {
            new DatePickerDialog(NewTrip.this, startDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            startDate = myCalendar.getTime();
        });
        endDateEdit.setOnClickListener(v -> {
            new DatePickerDialog(NewTrip.this, endDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            endDate = myCalendar.getTime();
        });
    }

    private void updateLabel(boolean isStart) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        if(isStart){
            startDateEdit.setText(sdf.format(myCalendar.getTime()));
        }else{
            endDateEdit.setText(sdf.format(myCalendar.getTime()));
        }
    }
}
