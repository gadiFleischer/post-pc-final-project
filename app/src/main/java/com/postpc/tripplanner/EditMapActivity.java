package com.postpc.tripplanner;
import static android.content.ContentValues.TAG;

import com.postpc.tripplanner.R;
import com.google.android.gms.common.api.ApiException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.postpc.tripplanner.models.EventModel;
import com.postpc.tripplanner.models.TripModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import android.location.Address;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EditMapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SupportMapFragment mapFragment;
    Geocoder geocoder;
    MyApp myApp;
    TripModel myTrip;
    LatLng searchedLocation=new LatLng(0,0);
    String searchedAddress="";
    int mMarkerCount = 0;
    Marker mMarker;
    Bitmap curBitmap = null;
    double lat;
    double lon;
    PlacesClient placesClient;
    int markerZoom = 15;

    ArrayList<EventModel> addedEvents ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_map_activity);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        geocoder = new Geocoder(this, Locale.getDefault());
        myApp = new MyApp(this);

        Intent getTripIntent=getIntent();
        String id= getTripIntent.getStringExtra("tripId");
        lat= getTripIntent.getDoubleExtra("lat",0.00);
        lon= getTripIntent.getDoubleExtra("long",0.00);
        myTrip=myApp.getTripById(id);
        addedEvents = myTrip.getEvents();

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);



        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setHint("Your location");
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteSupportFragment.getView().setBackgroundColor(Color.WHITE);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                if(mMarkerCount > 0){
                    removeMarker();
                }
                mMarkerCount++;
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
                searchedAddress = place.getAddress();
                String location = place.getName();

                if (location != null || !location.equals("")) {

                    LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    getBitmapByPlaceId(place.getId());
                    mMarker=map.addMarker(new MarkerOptions().position(latLng).title(location).
                            snippet(searchedAddress));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, markerZoom));
                    searchedLocation=latLng;
                }
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mapFragment.getMapAsync(this);
    }
    @Override
    public void onBackPressed() {
        Intent tripDetialsActivity = new Intent(this, TripDetails.class);
        tripDetialsActivity.putExtra("tripId", this.myTrip.id);
        this.startActivity(tripDetialsActivity);
        finish();
        super.onBackPressed();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng;
        int zoom = 13;
        if(lat!=0 && lon !=0){
            latLng= new LatLng(lat, lon);
        }else{
            try {
                if(addedEvents.size()==0){
                    double lat = geocoder.getFromLocationName(myTrip.destination,1).get(0).getLatitude();
                    double lon = geocoder.getFromLocationName(myTrip.destination,1).get(0).getLongitude();
                    latLng= new LatLng(lat, lon);
                    zoom = 7;
                }else{
                    LatLng defaultPos = addedEvents.get(0).position;
                    latLng= new LatLng(defaultPos.latitude, defaultPos.longitude);
                }
            } catch (IOException e) {
                e.printStackTrace();
                latLng= new LatLng(0, 0);
            }

        }
        CameraUpdate point = CameraUpdateFactory.newLatLng(latLng);
        populateLocations();
        map.moveCamera(point);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        map.setOnPoiClickListener(point12 -> {
            List<Address> addresses = null;
            String location="";
            try {
                addresses = geocoder.getFromLocation(point12.latLng.latitude, point12.latLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(mMarkerCount > 0){
                removeMarker();
            }
            mMarkerCount++;
            if(addresses!=null && addresses.size()>0){
                searchedAddress = addresses.get(0).getAddressLine(0);
                location = addresses.get(0).getAddressLine(0).split(",")[0];
            }
            getBitmapByPlaceId(point12.placeId);
            if (!location.equals("")) {
                mMarker=map.addMarker(new MarkerOptions().position(point12.latLng).title(location).
                        snippet(searchedAddress));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point12.latLng, markerZoom));
                searchedLocation= point12.latLng;
            }
        });

        map.setOnMapClickListener(point1 -> {
            List<Address> addresses = null;
            String location="";
            try {
                addresses = geocoder.getFromLocation(point1.latitude, point1.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(mMarkerCount > 0){
                removeMarker();
            }
            mMarkerCount++;
            if(addresses!=null && addresses.size()>0) {
                searchedAddress = addresses.get(0).getAddressLine(0);
                location = addresses.get(0).getAddressLine(0).split(",")[0];
            }
            if (!location.equals("")) {
                mMarker=map.addMarker(new MarkerOptions().position(point1).title(location).
                        snippet(searchedAddress));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point1, markerZoom));
                searchedLocation= point1;
            }
        });

        map.setOnMarkerClickListener((Marker marker) -> {
            LatLng pos = marker.getPosition();
            if(searchedLocation.latitude==pos.latitude && searchedLocation.longitude== pos.longitude) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.add_to_trip_window, null);
                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
                TextView title = popupView.findViewById(R.id.title_add_to_trip);
                TextView location1 = popupView.findViewById(R.id.location_add_to_trip);
                Button button = popupView.findViewById(R.id.button_add_to_trip);
                ImageView imageView = popupView.findViewById(R.id.imageView_add_to_trip);
                setPhotoToEvent(pos, imageView);
                title.setText(marker.getTitle());
                location1.setText(marker.getSnippet());
                button.setOnClickListener(v ->{
                    popupWindow.dismiss();
                    goToNewEventIntent(searchedAddress, marker.getPosition());
                });
                // dismiss the popup window when touched
                popupView.setOnTouchListener((v, event) -> {
                    popupWindow.dismiss();
                    return true;
                });
                return false;
            }else{
                int position = findEventByPos(pos);
                if(position==-1){
                    return false;
                }
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.go_to_edit_event_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
                TextView title = popupView.findViewById(R.id.title_edit_event);
                TextView location = popupView.findViewById(R.id.location_edit_event);
                Button button = popupView.findViewById(R.id.button_edit_event);
                ImageView imageView = popupView.findViewById(R.id.imageView_edit_event);
                setPhotoToEvent(pos, imageView);
                title.setText(addedEvents.get(position).name);
                location.setText(addedEvents.get(position).address);
                button.setOnClickListener(v ->{
                    popupWindow.dismiss();
                    goToEditEventIntent(addedEvents.get(position));
                });
                // dismiss the popup window when touched
                popupView.setOnTouchListener((v, event) -> {
                    popupWindow.dismiss();
                    finish();
                    return true;
                });
            }
            return false;
        });


    }

    public int findEventByPos(LatLng pos){
        for (int i = 0, addedEventsSize = addedEvents.size(); i < addedEventsSize; i++) {
            EventModel event = addedEvents.get(i);
            if (event.position.latitude == pos.latitude && event.position.longitude == pos.longitude) {
                return i;
            }
        }
        return -1;
    }

    public void populateLocations() {
        for (int i = 0; i < addedEvents.size(); i++) {
            EventModel event = addedEvents.get(i);
            float color = myApp.colors[event.day];
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(event.position)
                    .title(event.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));
            marker.setTag(i);
        }
    }

    private void setPhotoToEvent(LatLng pos, ImageView imageView){
        imageView.setImageResource(R.drawable.image_unavailable_foreground);
        int index = findEventByPos(pos);
        if (index != -1){
            EventModel event = addedEvents.get(index);
            if (event.bitmap != null){
                imageView.setImageBitmap(event.getEventImage());
            }
        }
        else{
            if (curBitmap != null){
                imageView.setImageBitmap(curBitmap);
            }
        }
    }


    public void goToNewEventIntent(String address, LatLng position){
        EventModel event = new EventModel("", address, null, position, 0, "", "", "", curBitmap);
        Intent addEventActivity = new Intent(this, NewEvent.class);

        //Convert to byte array
        byte[] byteArray=null;
        if(curBitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            curBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }
        addEventActivity.putExtra("image",byteArray);
        addEventActivity.putExtra("newEvent", (Serializable) event);
        addEventActivity.putExtra("tripId", this.myTrip.id);
        this.startActivity(addEventActivity);
        finish();
    }

    public void goToEditEventIntent(EventModel event){
        Intent editEventIntent = new Intent(this, EditEvent.class);
        editEventIntent.putExtra("eventId", event.id);
        editEventIntent.putExtra("tripId", this.myTrip.id);
        this.startActivity(editEventIntent);
        finish();
    }

    private void getBitmapByPlaceId(String placeId){
        List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);
        this.placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();
            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty()) {
                Log.w(TAG, "No photo metadata.");
                return;
            }
            final PhotoMetadata photoMetadata = metadata.get(0);

            // Get the attribution text.
            final String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                curBitmap = fetchPhotoResponse.getBitmap();
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                }
            });
        });
    }


    private void removeMarker(){
        if(mMarkerCount>0){
            mMarker.remove();
            mMarkerCount=0;
            curBitmap = null;
        }
    }
}
