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

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyCameraActivity extends Activity
{
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int GALLERY_REQUEST =1;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button cameraButton = (Button) this.findViewById(R.id.buttonOpenCamera);
        Button buttonFromMemory = (Button) this.findViewById(R.id.buttonFromMemory);
//        getMapImage(31.7749837,35.21760829999999);


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
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }



//    public Bitmap getMapImage(double latitude, double longitude) {
//
//        Bitmap bmp = null;
//        InputStream inputStream = null;
//        try {
//            java.net.URL mapUrl = new URL("https://maps.google.com/maps/api/staticmap?center="+latitude+","+longitude+"&zoom=15&size=200x200&sensor=false");
//
//            HttpURLConnection httpURLConnection = (HttpURLConnection) mapUrl.openConnection();
//
//            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
//
//            bmp = BitmapFactory.decodeStream(inputStream);
//
//            inputStream.close();
//            httpURLConnection.disconnect();
//
//        } catch (IllegalStateException e) {
//            Log.e("tag", e.toString());
//        } catch (IOException e) {
//            Log.e("tag", e.toString());
//        }
//        imageView.setImageBitmap(bmp);
//        return bmp;
//    }
}
