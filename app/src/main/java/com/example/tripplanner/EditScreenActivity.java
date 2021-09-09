package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.models.TripModel;

import java.io.Serializable;

//public class EditScreenActivity extends AppCompatActivity implements Serializable {
//    private TripModel tripModel;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
//        this.tripModel = (TripModel)intent.getSerializableExtra("tripId");
//        setContentView(R.layout.popup_window);

//        setContentView(R.layout.row_trip);
//        EditText descriptionView = findViewById(R.id.description);
//        TextView statusView = findViewById(R.id.status);
//        TextView dateModifiedView = findViewById(R.id.modified_task);
//r
//        descriptionView.setText(todoItem.getText());
////        dateCreatedView.setText(creationTimeTitle);
//        dateModifiedView.setText(setTimeToLatestAndGetTitle());
//        todoItem.modificationTime();
//
//        String title = "This task is in process";
//        if (this.todoItem.getStatus())
//        {
//            title = "This task is Done. Great job!";
//        }
//        statusView.setText(title);

//        descriptionView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                todoItem.setText(editable.toString());
//                setTimeToLatestAndGetTitle();
//                todoItem.modificationTime();
//            }
//        });
//
//    }
//}
