package com.example.tripplanner.models;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

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
    public Bitmap bitmap;

    public EventModel(String name,String address,CategoryEvent category, LatLng position
            ,int day,String startTime,String endTime,String comment){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.category = category;
        this.position = position;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
        this.bitmap = null;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
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
