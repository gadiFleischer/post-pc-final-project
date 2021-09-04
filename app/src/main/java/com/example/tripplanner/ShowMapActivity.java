package com.example.tripplanner;

import android.content.Intent;
import android.location.Geocoder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.tripplanner.models.CategoryEvent;
import com.example.tripplanner.models.EventModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    Geocoder geocoder;

    Intent intent = getIntent();

    EventModel[] addedEvents = { //TODO: not hardcoded options
            new EventModel("Pnina Pie", "Sweets", CategoryEvent.FOOD,new LatLng(31.777883, 35.198348), 2, "",  "", "comment"),
    };


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(31.777028899999998, 35.1980509));
        LatLng latLng = new LatLng(31.777028899999998, 35.1980509);
        Marker home = map.addMarker(new MarkerOptions().position(latLng).title("Home"));
        home.setTag(-1);
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
                            .resize(400, 200)
                            .into(imageView);
                    title.setText(addedEvents[position].name);
                    location.setText(addedEvents[position].address);
                    // TODO: click to get into event details
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            goToSaleIntent(position);
//                        }
//                    });
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
}
