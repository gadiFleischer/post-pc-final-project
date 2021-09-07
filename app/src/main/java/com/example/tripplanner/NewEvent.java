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
    String[] categoryItems = new String[]{"FOOD", "SIGHT", "HOTEL","OTHER"};
    TimePickerDialog picker;
    TripModel myTrip;
    MyApp myApp;
    String startTime;
    String endTime;


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
        EventModel event = (EventModel) getTripIntent.getSerializableExtra("newEvent");
        this.addressTitle.setText(event.address);

        ArrayAdapter<String> adapterCategorys = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        categoryDropdown.setAdapter(adapterCategorys);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.myTrip.daysDropdown);
        daysDropDown.setAdapter(adapterDays);



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
                    (tp, sHour, sMinute) -> endTimeEdit.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.show();
            this.endTime=endTimeEdit.getText().toString();
        });

        addNewEventButton.setOnClickListener(view -> {
            event.category=myApp.getCategoryFromString(categoryDropdown.getSelectedItem().toString());
            event.comment=this.commentEdit.getText().toString();
            event.startTime=this.startTime;
            event.endTime=this.endTime;
            event.name=this.nickNameEdit.getText().toString();
            String dayString = daysDropDown.getSelectedItem().toString();
            int day = dayString.equals("")? 0 : this.myTrip.dayToInt.get(dayString);
            event.day=day;
            this.myTrip.days.get(day).events.add(event);
            this.myApp.saveTrip(myTrip);

            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
        });

    }
}
