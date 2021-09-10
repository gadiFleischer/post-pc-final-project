package com.example.tripplanner;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
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

import java.util.Arrays;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditMapActivity extends FragmentActivity implements OnMapReadyCallback {
    Button editEventMarkerButton;

    GoogleMap map;
    SupportMapFragment mapFragment;
    Geocoder geocoder;

    EventModel[] addedEvents = { //TODO: not hardcoded options
            new EventModel("Pnina Pie", "Sweets", CategoryEvent.FOOD,new LatLng(31.777883, 35.198348), 2, "", "", "comment"),

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_map_activity);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        geocoder = new Geocoder(this, Locale.getDefault());

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


            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
                String address = place.getAddress();
                System.out.println("*** check " + place + " ***");
                String location = place.getName();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(EditMapActivity.this);
                    try {
                        addressList = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address latlong = addressList.get(0);
                    LatLng latLng = new LatLng(latlong.getLatitude(), latlong.getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(location).
                            snippet(address));
                    marker.setTag(0);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        if ((int) (marker.getTag()) == -1) {
                            return false;
                        } else {
                            final int position = (int) (marker.getTag());
                            Log.d("TAG", addedEvents[position].name);
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
                            TextView location = popupView.findViewById(R.id.location_add_to_trip);
                            Button button = popupView.findViewById(R.id.button_add_to_trip);
                            ImageView imageView = popupView.findViewById(R.id.imageView_add_to_trip);
                            Picasso.get().load(addedEvents[position].comment).centerCrop()
                                    .resize(400, 400)
                                    .into(imageView);
                            title.setText(marker.getTitle());
//                            location.setText(marker.getPosition().toString());

                            location.setText(marker.getSnippet());
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goToNewEventIntent(marker.getTitle(), address, marker.getPosition());
                                }
                            });

                            // dismiss the popup window when touched
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });
                        }
                        return false;
                    }
                });


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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(31.777028899999998, 35.1980509);
        CameraUpdate point = CameraUpdateFactory.newLatLng(latLng);

//        Marker home = map.addMarker(new MarkerOptions().position(latLng).title("Home"));
//        home.setTag(-1);
        populateLocations();
        // moves camera to coordinates
        map.moveCamera(point);
        // animates camera to coordinates
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if ((int) (marker.getTag()) == -1) {
                    return false;
                } else {
                    final int position = (int) (marker.getTag());
                    Log.d("TAG", addedEvents[position].name);
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
                    Picasso.get().load(addedEvents[position].comment).centerCrop()
                            .resize(400, 400)
                            .into(imageView);
                    title.setText(addedEvents[position].name);
                    location.setText(addedEvents[position].address);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToEditEventIntent(addedEvents[position]);
                        }
                    });
                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                }
                return false;
            }
        });
    }

    public void populateLocations() {
        float iconColor = BitmapDescriptorFactory.HUE_RED;
        for (int i = 0; i < addedEvents.length; i++) {
            EventModel l = addedEvents[i];
            if (l.category.equals("Sweets")) { //TODO: refactor this shit
                iconColor = BitmapDescriptorFactory.HUE_ROSE;
            } else if (l.category.equals("Farm")) {
                iconColor = BitmapDescriptorFactory.HUE_CYAN;
            } else if (l.category.equals("Drinks")) {
                iconColor = BitmapDescriptorFactory.HUE_GREEN;
            } else if (l.category.equals("Homemade")) {
                iconColor = BitmapDescriptorFactory.HUE_AZURE;
            }
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(l.position)
                    .title(l.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor)));
            marker.setTag(i);
        }

    }

    public void goToNewEventIntent(String name, String address, LatLng position){
        Intent newEventIntent = new Intent(this, NewEvent.class);
        EventModel event = new EventModel(name, address, null, position, 1, null, null, null);
        newEventIntent.putExtra("event item", (Parcelable) event);
        this.startActivity(newEventIntent);
    }

    public void goToEditEventIntent(EventModel event){
        Intent editEventIntent = new Intent(this, EditEvent.class);
        editEventIntent.putExtra("event item", (Parcelable) event);
        this.startActivity(editEventIntent);
    }



}
