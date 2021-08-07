package com.example.tripplanner;
import java.util.Date;
import java.util.UUID;

public class EventModel {

    public String id;
    public String name;
    public String address;
    public CategoryEvent category;
    public int day;
    public Date startTime;
    public Date endTime;
    public String comment;
    public long lat;
    public long lang;

    public EventModel(String name,String address,CategoryEvent category
            ,int day,Date startTime,Date endTime,String comment){

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.category = category;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
    }
}
