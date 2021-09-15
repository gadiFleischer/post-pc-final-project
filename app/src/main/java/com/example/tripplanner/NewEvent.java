package com.example.tripplanner;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.util.Calendar;

public class NewEvent extends AppCompatActivity {
    Button addNewEventButton;
    TextView addressTitle;
    EditText nickNameEdit;
    EditText startTimeEdit;
    EditText endTimeEdit;
    EditText commentEdit;
    TimePickerDialog picker;
    TripModel myTrip;
    MyApp myApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event_activity);
        addNewEventButton = findViewById(R.id.addThisEventButton);
        Spinner categoryDropdown = findViewById(R.id.categorySpinner);
        addressTitle = findViewById(R.id.addressTitle);
        nickNameEdit = findViewById(R.id.nickNameEdit);
        Spinner daysDropDown = findViewById(R.id.daySpinner);
        startTimeEdit = findViewById(R.id.startTimeEdit);
        endTimeEdit = findViewById(R.id.endTimeEdit);
        commentEdit = findViewById(R.id.commentEdit);
        final Calendar cldr = Calendar.getInstance();

        myApp = new MyApp(this);
        Intent getTripIntent=getIntent();
        this.myTrip = myApp.getTripById(getTripIntent.getStringExtra("tripId"));
        EventModel event = myTrip.getEventById(getTripIntent.getStringExtra("eventId"));

        ArrayAdapter<String> adapterCategorys = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, myApp.categoryItems);
        categoryDropdown.setAdapter(adapterCategorys);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.myTrip.daysDropdown);
        daysDropDown.setAdapter(adapterDays);
        addressTitle.setText("Address: "+event.address);

        startTimeEdit.setInputType(InputType.TYPE_NULL);
        startTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(NewEvent.this,
                    (tp, sHour, sMinute) -> startTimeEdit.setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, true);
            picker.show();
        });

        endTimeEdit.setInputType(InputType.TYPE_NULL);
        endTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(NewEvent.this,
                    (tp, sHour, sMinute) -> endTimeEdit.setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, true);
            picker.show();
        });

        addNewEventButton.setOnClickListener(view -> {
            event.category=myApp.getCategoryFromString(categoryDropdown.getSelectedItem().toString());
            event.comment=this.commentEdit.getText().toString();
            event.name=this.nickNameEdit.getText().toString();
            event.startTime=startTimeEdit.getText().toString();
            event.endTime=endTimeEdit.getText().toString();
            String dayString = daysDropDown.getSelectedItem().toString();
            if(event.name.equals("") || event.endTime.compareTo(event.startTime)<0){
                Toast toast = Toast.makeText(this,"your inputs are invalid", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if(myTrip.isNameTaken(event)){
                Toast toast = Toast.makeText(this,"name already taken", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            int day = dayString.equals("")? 0 : this.myTrip.dayToInt.get(dayString);
            event.day=day;
            this.myTrip.days.get(day).events.add(event);
            this.myApp.saveTrip(myTrip);

            Intent editMapActivity = new Intent(this, EditMapActivity.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            editMapActivity.putExtra("lat", event.position.latitude);
            editMapActivity.putExtra("long", event.position.longitude);
            this.startActivity(editMapActivity);
            finish();
        });

    }
}
