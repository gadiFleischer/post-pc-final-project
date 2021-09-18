package com.example.tripplanner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;
import java.io.Serializable;
import java.util.Calendar;

public class EditEvent extends AppCompatActivity implements Serializable {
    Button deleteEditButton;
    Button doneEditButton;
    Button editImageButton;
    TextView addressTitle;
    EditText nickNameEdit;
    EditText startTimeEdit;
    EditText endTimeEdit;
    EditText commentEdit;
    TimePickerDialog picker;
    TripModel myTrip;
    EventModel event;
    MyApp myApp;
    int lastDay;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activty);

        deleteEditButton = findViewById(R.id.deleteEditButton);
        doneEditButton = findViewById(R.id.DoneEditButton);
        editImageButton = findViewById(R.id.editImageButton);
        Spinner categoryDropdown = findViewById(R.id.categorySpinnerEditEvent);
        addressTitle = findViewById(R.id.addressTitleEditEvent);
        nickNameEdit = findViewById(R.id.nickNameEditEditEvent);
        Spinner daysDropDown = findViewById(R.id.daySpinnerEditEvent);
        startTimeEdit = findViewById(R.id.startTimeEditEditEvent);
        endTimeEdit = findViewById(R.id.endTimeEditEvent);
        commentEdit = findViewById(R.id.commentEditEvent);
        final Calendar cldr = Calendar.getInstance();

        myApp = new MyApp(this);
        Intent getTripIntent=getIntent();
        this.myTrip = myApp.getTripById(getTripIntent.getStringExtra("tripId"));
        this.event = myTrip.getEventById(getTripIntent.getStringExtra("eventId"));
        if (this.event == null){
            myApp.loadTempEvent();
            event = myApp.curTempEvent;
        }
        //Edit fields
        this.addressTitle.setText("Address: " + event.address);
        this.nickNameEdit.setText(event.name);
        this.startTimeEdit.setText(event.startTime);
        this.endTimeEdit.setText(event.endTime);
        this.commentEdit.setText(event.comment);
        lastDay= event.day;

        ArrayAdapter<String> adapterCategorys = new ArrayAdapter<>(this, R.layout.selected_item_in_spinner, myApp.categoryItems);
        adapterCategorys.setDropDownViewResource(R.layout.spinner_item);
        categoryDropdown.setAdapter(adapterCategorys);
        categoryDropdown.setSelection(event.category.ordinal());

        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, R.layout.selected_item_in_spinner, this.myTrip.daysDropdown);
        adapterDays.setDropDownViewResource(R.layout.spinner_item);
        daysDropDown.setAdapter(adapterDays);
        daysDropDown.setSelection(event.day);

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
            this.event.endTime=endTimeEdit.getText().toString();
        });

        doneEditButton.setOnClickListener(view -> {
            event.category=myApp.getCategoryFromString(categoryDropdown.getSelectedItem().toString());
            event.comment=this.commentEdit.getText().toString();
            event.name=this.nickNameEdit.getText().toString();
            String dayString = daysDropDown.getSelectedItem().toString();
            event.startTime=startTimeEdit.getText().toString();
            event.endTime=endTimeEdit.getText().toString();
            if(event.name.equals("")){
                Toast toast = Toast.makeText(this,"Event name Cant be empty", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if(myTrip.isNameTaken(event)){
                Toast toast = Toast.makeText(this,"name already taken", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if(event.endTime.compareTo(event.startTime)<0){
                Toast toast = Toast.makeText(this,"start time can't be bigger than end time", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            int day = dayString.equals("")? 0 : this.myTrip.dayToInt.get(dayString);
            if(lastDay!=day){
                myTrip.days.get(lastDay).events.removeIf(x->x.id.equals(event.id));
                myTrip.days.get(day).events.add(event);
                event.day=day;
            }else{
                int index = MyApp.getEventByIdIndex(this.myTrip,day, event.id);
                if(index!=-1){
                    this.myTrip.days.get(day).events.set(index, event);
                }
            }
            this.myApp.saveTrip(myTrip);


            Intent editMapActivity = new Intent(this, EditMapActivity.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            editMapActivity.putExtra("lat", event.position.latitude);
            editMapActivity.putExtra("long", event.position.longitude);
            this.startActivity(editMapActivity);
            finish();
        });
        deleteEditButton.setOnClickListener(view -> {
            int day = event.day;
            int index = MyApp.getEventByIdIndex(this.myTrip,day, event.id);
            if(index!=-1){
                this.myTrip.days.get(day).events.remove(index);
            }
            this.myApp.saveTrip(myTrip);
            Intent editMapActivity = new Intent(this, EditMapActivity.class);
            editMapActivity.putExtra("tripId", this.myTrip.id);
            this.startActivity(editMapActivity);
            finish();
        });
        editImageButton.setOnClickListener(view -> {
            event.category= myApp.getCategoryFromString(categoryDropdown.getSelectedItem().toString());
            event.comment=this.commentEdit.getText().toString();
            event.name=this.nickNameEdit.getText().toString();
            event.startTime=startTimeEdit.getText().toString();
            event.endTime=endTimeEdit.getText().toString();
            myApp.curTempEvent = event;
            myApp.saveTempEvent();
            Intent editImageActivity = new Intent(this, MyCameraActivity.class);
            editImageActivity.putExtra("tripId", this.myTrip.id);
            editImageActivity.putExtra("activity", "edit");
            this.startActivity(editImageActivity);
            finish();
        });
    }
    @Override
    public void onBackPressed() {
        Intent editMapActivity = new Intent(this, EditMapActivity.class);
        editMapActivity.putExtra("tripId", this.myTrip.id);
        editMapActivity.putExtra("lat", event.position.latitude);
        editMapActivity.putExtra("long", event.position.longitude);
        this.startActivity(editMapActivity);
        finish();
        super.onBackPressed();
    }
}
