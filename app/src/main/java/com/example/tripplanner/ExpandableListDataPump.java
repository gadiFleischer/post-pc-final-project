package com.example.tripplanner;

import com.example.tripplanner.models.DayModel;
import com.example.tripplanner.models.EventModel;
import com.example.tripplanner.models.TripModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData(TripModel myTrip) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();
        String eventStr="";
        String dayStr="";
        for (DayModel day : myTrip.days)
        {
            List<String> dayList = new ArrayList<>();
            ArrayList<EventModel> events = day.events;
            Collections.sort(events, (o1, o2) -> o1.startTime.compareTo(o2.startTime));

            for (EventModel event: events) {
                eventStr = event.name+" | At: "+event.startTime+" - "+event.endTime;
                dayList.add(eventStr);
            }
            dayStr="Day "+day.dayNum+": "+day.dayString;

            expandableListDetail.put(dayStr, dayList);
        }
        return expandableListDetail;
    }
}
