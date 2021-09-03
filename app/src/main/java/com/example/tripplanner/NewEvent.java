package com.example.tripplanner;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.EventLog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.util.Calendar;
import java.util.Date;

public class NewEvent extends AppCompatActivity {
    Button addNewEventButton;
    TextView addressTitle;
    EditText nickNameEdit;
    EditText dayEdit;
    EditText startTimeEdit;
    EditText endTimeEdit;
    EditText commentEdit;
    String[] categoryItems = new String[]{"FOOD", "SIGHT", "HOTEL","OTHER"};
    //TODO: create picker for days
    TimePickerDialog picker;

    TripModel myTrip;
    MyApp myApp;

    int day=0;
    String startTime;
    String endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event_activity);
        addNewEventButton = findViewById(R.id.addThisEventButton);
        Spinner dropdown = findViewById(R.id.categorySpinner);
        addressTitle = findViewById(R.id.addressTitle);
        nickNameEdit = findViewById(R.id.nickNameEdit);
        dayEdit = findViewById(R.id.dayEdit);
        startTimeEdit = findViewById(R.id.startTimeEdit);
        endTimeEdit = findViewById(R.id.endTimeEdit);
        commentEdit = findViewById(R.id.commentEdit);
        final Calendar cldr = Calendar.getInstance();

        myApp = new MyApp(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        dropdown.setAdapter(adapter);


        Intent getTripIntent=getIntent();
        this.myTrip = myApp.getTripById(getTripIntent.getStringExtra("tripId"));
        EventModel event = (EventModel) getTripIntent.getSerializableExtra("newEvent");
        this.addressTitle.setText(event.address);

        startTimeEdit.setInputType(InputType.TYPE_NULL);
        startTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(NewEvent.this,
                    (tp, sHour, sMinute) -> startTimeEdit.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.show();
            this.startTime=startTimeEdit.getText().toString();
        });

        endTimeEdit.setInputType(InputType.TYPE_NULL);
        endTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(NewEvent.this,
                    (tp, sHour, sMinute) -> {
                        endTimeEdit.setText(sHour + ":" + sMinute);
                    }, hour, minutes, true);
            picker.show();
            this.endTime=startTimeEdit.getText().toString();
        });

        addNewEventButton.setOnClickListener(view -> {
            event.category=myApp.getCategoryFromString(dropdown.getSelectedItem().toString());
            event.comment=this.commentEdit.getText().toString();
            event.startTime=this.startTime;
            event.endTime=this.endTime;
            String dayString = this.dayEdit.getText().toString();
            int day = dayString.equals("")?0:Integer.parseInt(dayString);
            this.myTrip.days.get(day).events.add(event);

            this.myApp.saveTrip(myTrip);

            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
        });

    }
}
