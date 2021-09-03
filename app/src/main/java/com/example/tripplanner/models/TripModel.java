package com.example.tripplanner.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TripModel implements Serializable {
    public String id;
    public String title;
    public String destination;
    public Date startDate;
    public Date endDate;
    public String countryCode;
    public ArrayList<DayModel> days;

    public TripModel(String title,String destination,Date startDate,Date endDate,String countryCode){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countryCode = countryCode;
        this.days= new ArrayList<>();
    }

    public void initDaysArray(){
        long diff = endDate.getTime() - startDate.getTime();
        long res= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;
        for (int i=0;i<res;i++){
            this.days.add(new DayModel(i+1,"red"));//TODO:think about color default
        }
    }
}
