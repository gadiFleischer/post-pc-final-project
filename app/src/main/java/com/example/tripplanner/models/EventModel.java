package com.example.tripplanner.models;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.widget.ImageView;

import com.example.tripplanner.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class EventModel implements Parcelable, Serializable {

    public String id;
    public String name;
    public String address;
    public CategoryEvent category;
    public int day;
    public String startTime;
    public String endTime;
    public String comment;
    public LatLng position;
    public String bitmap;

    public EventModel(String name,String address,CategoryEvent category, LatLng position
            ,int day,String startTime,String endTime,String comment, Bitmap bitmap){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.category = category;
        this.position = position;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
        this.bitmap = convertBitmapToString(bitmap);
    }

    private String convertBitmapToString(Bitmap bitmap){
        if (bitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        return "";
    }

    public void setEventImage(Bitmap bitmap) {
        this.bitmap = convertBitmapToString(bitmap);
    }

    public Bitmap getEventImage() {
        if (bitmap != null){
            byte[] imageAsBytes = Base64.decode(bitmap.getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }
        return null;

    }

    protected EventModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        day = in.readInt();
        comment = in.readString();
        position = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeInt(day);
        dest.writeString(comment);
        dest.writeParcelable(position,flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {
            return new EventModel(in);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };
}
