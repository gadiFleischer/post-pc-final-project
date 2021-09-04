package com.example.tripplanner;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.util.Calendar;

public class EditEvent extends AppCompatActivity {
    Button deleteEditButton;
    Button DoneEditButton;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activty);

        deleteEditButton = findViewById(R.id.deleteEditButton);
        DoneEditButton = findViewById(R.id.DoneEditButton);
        Spinner categoryDropdown = findViewById(R.id.categorySpinnerEditEvent);
        addressTitle = findViewById(R.id.addressTitleEditEvent);
        nickNameEdit = findViewById(R.id.nickNameEditEvent);
        Spinner daysDropDown = findViewById(R.id.daySpinnerEditEvent);
        startTimeEdit = findViewById(R.id.startTimeEditEditEvent);
        endTimeEdit = findViewById(R.id.endTimeEditEvent);
        commentEdit = findViewById(R.id.commentEditEvent);
        final Calendar cldr = Calendar.getInstance();

        myApp = new MyApp(this);
        Intent getTripIntent=getIntent();
        this.myTrip = myApp.getTripById(getTripIntent.getStringExtra("tripId"));
        EventModel event = (EventModel) getTripIntent.getSerializableExtra("newEvent");
        //Edit fields
        this.addressTitle.setText(event.address);
        this.nickNameEdit.setText(event.name);
        this.startTimeEdit.setText(event.startTime);
        this.endTimeEdit.setText(event.endTime);
        this.commentEdit.setText(event.comment);
        this.startTime=event.startTime;
        this.endTime=event.endTime;

        //TODO: set adapters with given input
        //TODO: fix bugfix for time
        ArrayAdapter<String> adapterCategorys = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        categoryDropdown.setAdapter(adapterCategorys);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.myTrip.daysDropdown);
        daysDropDown.setAdapter(adapterDays);

        startTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(EditEvent.this,
                    (tp, sHour, sMinute) -> startTimeEdit.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.show();
            this.startTime=startTimeEdit.getText().toString();
        });

        endTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(EditEvent.this,
                    (tp, sHour, sMinute) -> endTimeEdit.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.show();
            this.endTime=endTimeEdit.getText().toString();
        });

        DoneEditButton.setOnClickListener(view -> {
            event.category=myApp.getCategoryFromString(categoryDropdown.getSelectedItem().toString());
            event.comment=this.commentEdit.getText().toString();
            event.startTime=this.startTime;
            event.endTime=this.endTime;
            event.name=this.nickNameEdit.getText().toString();
            String dayString = daysDropDown.getSelectedItem().toString();
            int day = dayString.equals("")? 0 : this.myTrip.dayToInt.get(dayString);
            int index = MyApp.getEventByIdIndex(this.myTrip,day,event.id);
            if(index!=-1){
                this.myTrip.days.get(day).events.set(index,event);
            }
            this.myApp.saveTrip(myTrip);
            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
        });
        deleteEditButton.setOnClickListener(view -> {
            String dayString = daysDropDown.getSelectedItem().toString();
            int day = dayString.equals("")? 0 : this.myTrip.dayToInt.get(dayString);
            int index = MyApp.getEventByIdIndex(this.myTrip,day,event.id);
            if(index!=-1){
                this.myTrip.days.get(day).events.remove(index);
            }
            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
        });
    }
}
