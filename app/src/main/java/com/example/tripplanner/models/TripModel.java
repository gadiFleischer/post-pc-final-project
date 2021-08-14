package com.example.tripplanner.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class TripModel implements Serializable {
    public String id;
    public String title;
    public String destination;
    public Date startDate;
    public Date endDate;
    public String countryCode; //this is for the flag
    public ArrayList<DayModel> days;

    public TripModel(String title,String destination,Date startDate,Date endDate,String countryCode){
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countryCode = countryCode;
        this.id = UUID.randomUUID().toString();
        this.days= new ArrayList<>();
    }
}
