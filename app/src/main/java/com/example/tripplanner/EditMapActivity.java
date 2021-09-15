package com.example.tripplanner;
import com.example.tripplanner.models.DayModel;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.location.Address;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditMapActivity extends FragmentActivity implements OnMapReadyCallback {

    Button editEventMarkerButton;

    GoogleMap map;
    SupportMapFragment mapFragment;
    Geocoder geocoder;
    MyApp myApp;
    TripModel myTrip;
    LatLng searchedLocation=new LatLng(0,0);
    String searchedAddress="";
    int mMarkerCount = 0;
    Marker mMarker;
    double lat;
    double lon;

    ArrayList<EventModel> addedEvents ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_map_activity);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        geocoder = new Geocoder(this, Locale.getDefault());
        myApp = new MyApp(this);

        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

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

        PlacesClient placesClient = Places.createClient(this);



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
                    mMarker.remove();
                    mMarkerCount=0;
                }
                mMarkerCount++;
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
                searchedAddress = place.getAddress();
                String location = place.getName();

                if (location != null || !location.equals("")) {

                    LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                    mMarker=map.addMarker(new MarkerOptions().position(latLng).title(location).
                            snippet(searchedAddress));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
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
        Intent tripDetails = new Intent(this, NewEvent.class);
        tripDetails.putExtra("tripId", this.myTrip.id);
        finish();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng;
        if(lat!=0 && lon !=0){
            latLng= new LatLng(lat, lon);
        }else{
            try {
                if(addedEvents.size()==0){
                    //TODO: karin here I get the location of the country and you need to play with the zoom
                    double lat = geocoder.getFromLocationName(myTrip.destination,1).get(0).getLatitude();
                    double lon = geocoder.getFromLocationName(myTrip.destination,1).get(0).getLongitude();
                    latLng= new LatLng(lat, lon);
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



        map.setOnMapClickListener(point1 -> {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(point1.latitude, point1.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(mMarkerCount > 0){
                mMarker.remove();
                mMarkerCount=0;
            }
            mMarkerCount++;
//                Toast.makeText(getApplicationContext(), addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
            searchedAddress = addresses.get(0).getAddressLine(0);
            String location = addresses.get(0).getAddressLine(0).split(",")[0];

            if (location != null || !location.equals("")) {
                mMarker=map.addMarker(new MarkerOptions().position(point1).title(location).
                        snippet(searchedAddress));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point1, 15));
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
                Picasso.get().load(marker.getSnippet()).centerCrop()
                        .resize(400, 400)
                        .into(imageView);
                title.setText(marker.getTitle());

                location1.setText(marker.getSnippet());
                button.setOnClickListener(v -> goToNewEventIntent(searchedAddress, marker.getPosition()));

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
                Picasso.get().load(addedEvents.get(position).name).centerCrop()
                        .resize(400, 400)
                        .into(imageView);
                title.setText(addedEvents.get(position).name);
                location.setText(addedEvents.get(position).address);
                button.setOnClickListener(v -> goToEditEventIntent(addedEvents.get(position)));
                // dismiss the popup window when touched
                popupView.setOnTouchListener((v, event) -> {
                    popupWindow.dismiss();
                    finish();
                    return true;
                });
            }
            return false;
        });

        populateLocations();
        map.moveCamera(point);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
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
        float iconColor = BitmapDescriptorFactory.HUE_RED;

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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void goToNewEventIntent(String address, LatLng position){
        EventModel event = new EventModel("", address, null, position, 0, "", "", "");
        Intent addEventActivity = new Intent(this, NewEvent.class);
        addEventActivity.putExtra("newEvent", (Serializable) event);
        addEventActivity.putExtra("tripId", this.myTrip.id);
        this.startActivity(addEventActivity);
    }

    public void goToEditEventIntent(EventModel event){
        Intent editEventIntent = new Intent(this, EditEvent.class);
        editEventIntent.putExtra("eventId", event.id);
        editEventIntent.putExtra("tripId", this.myTrip.id);
        this.startActivity(editEventIntent);
    }



}
