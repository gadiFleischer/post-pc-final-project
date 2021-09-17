package com.example.tripplanner;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.Serializable;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity {
    Button addNewEventButton;
    Button editImgAddEvent;
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
        editImgAddEvent = findViewById(R.id.editImgAddEvent);
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
        EventModel event =(EventModel) getTripIntent.getSerializableExtra("newEvent");
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        if(byteArray!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            event.setEventImage(bitmap);
        }

        ArrayAdapter<String> adapterCategorys = new ArrayAdapter<>(this, R.layout.spinner_item, myApp.categoryItems);
        categoryDropdown.setAdapter(adapterCategorys);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, R.layout.spinner_item, this.myTrip.daysDropdown);
        daysDropDown.setAdapter(adapterDays);
        addressTitle.setText("Address: " + event.address);

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
        editImgAddEvent.setOnClickListener(view -> {
            Intent editImageActivity = new Intent(this, MyCameraActivity.class);
            editImageActivity.putExtra("tripId", this.myTrip.id);
            editImageActivity.putExtra("newEvent", (Serializable) event);
            editImageActivity.putExtra("activity", "add");
            this.startActivity(editImageActivity);
            finish();
        });

    }
}
