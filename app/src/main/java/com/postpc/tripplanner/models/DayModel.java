package com.postpc.tripplanner.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DayModel implements Serializable {
    public int dayNum;
    public String color;
    public String dayString="";
    public ArrayList<EventModel> events;

    public DayModel(int dayNum,String color){
        this.dayNum = dayNum;
        this.color = color;
        this.events = new ArrayList<>();
    }




}
