package com.example.tripplanner;
import java.util.Date;

public class EventModel {

    public String id;
    public String address;
    public CategoryEvent category;
    public Date day;
    public Date startTime;
    public Date endTime;
    public String comment;
    public long lat;
    public long lang;

    public EventModel(String id,String address,CategoryEvent category
            ,Date day,Date startTime,Date endTime,String comment){

        this.id = id;
        this.address = address;
        this.category = category;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
    }
}
