package com.example.tripplanner;

import com.example.tripplanner.models.DayModel;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData(TripModel myTrip) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();
        for (DayModel day : myTrip.days)
        {
            List<String> dayList = new ArrayList<>();
            for (EventModel event: day.events) {
                dayList.add(event.name);
            }
            String dayStr="Day number: "+day.dayNum+" "+day.dayString;
            expandableListDetail.put(dayStr, dayList);
        }
        return expandableListDetail;
    }
}
