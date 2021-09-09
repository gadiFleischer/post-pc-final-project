package com.example.tripplanner;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.io.Serializable;
import java.util.Calendar;

public class EditEvent extends AppCompatActivity implements Serializable {
    Button deleteEditButton;
    Button DoneEditButton;
    TextView addressTitle;
    EditText nickNameEdit;
    EditText startTimeEdit;
    EditText endTimeEdit;
    EditText commentEdit;
    TimePickerDialog picker;
    TripModel myTrip;
    EventModel myEvent;
    MyApp myApp;
    int lastDay;

    @SuppressLint("DefaultLocale")
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
        this.myEvent = myTrip.getEventById(getTripIntent.getStringExtra("EventId"));
        //Edit fields
        this.addressTitle.setText(myEvent.address);
        this.nickNameEdit.setText(myEvent.name);
        this.startTimeEdit.setText(myEvent.startTime);
        this.endTimeEdit.setText(myEvent.endTime);
        this.commentEdit.setText(myEvent.comment);
        lastDay=myEvent.day;

        ArrayAdapter<String> adapterCategorys = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, myApp.categoryItems);
        categoryDropdown.setAdapter(adapterCategorys);
        categoryDropdown.setSelection(myEvent.category.ordinal());

        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.myTrip.daysDropdown);
        daysDropDown.setAdapter(adapterDays);
        daysDropDown.setSelection(myEvent.day);

        startTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(EditEvent.this,
                    (tp, sHour, sMinute) -> startTimeEdit.setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, true);
            picker.show();
        });

        endTimeEdit.setOnClickListener(v -> {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            picker = new TimePickerDialog(EditEvent.this,
                    (tp, sHour, sMinute) -> endTimeEdit.setText(String.format("%02d:%02d", sHour, sMinute)), hour, minutes, true);
            picker.show();
            this.myEvent.endTime=endTimeEdit.getText().toString();
        });

        DoneEditButton.setOnClickListener(view -> {
            myEvent.category=myApp.getCategoryFromString(categoryDropdown.getSelectedItem().toString());
            myEvent.comment=this.commentEdit.getText().toString();
            myEvent.name=this.nickNameEdit.getText().toString();
            String dayString = daysDropDown.getSelectedItem().toString();
            myEvent.startTime=startTimeEdit.getText().toString();
            myEvent.endTime=endTimeEdit.getText().toString();
            if(myEvent.name.equals("") || myEvent.endTime.compareTo(myEvent.startTime)<0){
                Toast toast = Toast.makeText(this,"your inputs are invalid", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            int day = dayString.equals("")? 0 : this.myTrip.dayToInt.get(dayString);
            if(lastDay!=day){
                myTrip.days.get(lastDay).events.removeIf(x->x.id.equals(myEvent.id));
                myTrip.days.get(day).events.add(myEvent);
                myEvent.day=day;
            }else{
                int index = MyApp.getEventByIdIndex(this.myTrip,day,myEvent.id);
                if(index!=-1){
                    this.myTrip.days.get(day).events.set(index,myEvent);
                }
            }
            this.myApp.saveTrip(myTrip);


            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
        });
        deleteEditButton.setOnClickListener(view -> {
            int day = myEvent.day;
            int index = MyApp.getEventByIdIndex(this.myTrip,day,myEvent.id);
            if(index!=-1){
                this.myTrip.days.get(day).events.remove(index);
            }
            this.myApp.saveTrip(myTrip);
            Intent editMapActivity = new Intent(this, EditMap.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
        });
    }
}
