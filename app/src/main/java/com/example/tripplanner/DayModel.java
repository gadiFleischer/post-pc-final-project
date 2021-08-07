package com.example.tripplanner;

import java.util.ArrayList;

public class DayModel {
    public int dayNum;
    public String color;
    public ArrayList<EventModel> events;

    public DayModel(int dayNum,String color){
        this.dayNum = dayNum;
        this.color = color;
        this.events = new ArrayList<>();
    }




}
