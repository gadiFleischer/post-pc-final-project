package com.example.tripplanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class MyCameraActivity extends Activity
{
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int GALLERY_REQUEST =1;
    MyApp myApp;
    TripModel myTrip;
    EventModel event;
    Bitmap curBitMap=null;
    String activity;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button cameraButton = (Button) this.findViewById(R.id.buttonOpenCamera);
        Button buttonFromMemory = (Button) this.findViewById(R.id.buttonFromMemory);
        Button returnToActivity = (Button) this.findViewById(R.id.returnToActivity);


        myApp=new MyApp(this);
        Intent getTripIntent=getIntent();
        myTrip = myApp.getTripById(getTripIntent.getStringExtra("tripId"));
        activity = getTripIntent.getStringExtra("activity");
        myApp.loadTempEvent();
        event = myApp.curTempEvent;

        if(!event.bitmap.equals("")){
            this.imageView.setImageBitmap(event.getEventImage());
        }else{
            imageView.setImageResource(R.drawable.image_unavailable_foreground);
        }


        cameraButton.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        buttonFromMemory.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        returnToActivity.setOnClickListener(view -> {
            if(curBitMap!=null){
                event.setEventImage(curBitMap);
            }
            Intent backToIntent;
            if(activity.equals("edit")){
                backToIntent = new Intent(this, EditEvent.class);
                myApp.curTempEvent = event;
                myApp.saveTempEvent();
//                backToIntent.putExtra("eventId", event.id);

            }else{ //ADD EVENT
                backToIntent = new Intent(this, NewEvent.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (curBitMap != null){
                    event.getEventImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    backToIntent.putExtra("image",byteArray);

                }
                myApp.curTempEvent = event;
                myApp.saveTempEvent();
            }
            backToIntent.putExtra("tripId", this.myTrip.id);
            myApp.saveTrip(myTrip);
            this.startActivity(backToIntent);
            finish();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
                curBitMap=photo;
            }
            else if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    curBitMap=selectedImage;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
