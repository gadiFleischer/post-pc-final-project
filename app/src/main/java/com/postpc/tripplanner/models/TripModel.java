package com.postpc.tripplanner.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TripModel implements Serializable{
    public String id;
    public String title;
    public String destination;
    public Date startDate;
    public Date endDate;
    public String countryCode;
    public ArrayList<DayModel> days;
    public String[] daysDropdown;
    public HashMap<String,Integer> dayToInt;

    public TripModel(String title,String destination,Date startDate,Date endDate,String countryCode){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countryCode = countryCode;
        this.days= new ArrayList<>();
        this.dayToInt = new HashMap<>();
    }

    public void initDaysArrayAndPicker() throws ParseException {
        long diff = endDate.getTime() - startDate.getTime();
        long res= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;
        for (int i=0;i<res;i++){
            this.days.add(new DayModel(i+1,"red"));
        }
        this.daysDropdown = new String[(int)res];
        this.dayToInt = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date curDate=startDate;
        for (int i=0;i<res;i++){
            String dayStr = dateFormat.format(curDate);
            daysDropdown[i]=dayStr;
            dayToInt.put(dayStr,i);
            days.get(i).dayString=dayStr;
            curDate = addDays(curDate, 1);
        }
    }

    public void setDaysArrayAndPicker() throws ParseException {
        long len;
        long diff = endDate.getTime() - startDate.getTime();
        long res= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;
        ArrayList<DayModel> newDays = new ArrayList<>();
        for (int i=0;i<res;i++){
            newDays.add(new DayModel(i+1,"red"));
        }
        if (res >= days.size()){
            len = days.size();
        }
        else{
            len = res;
        }
        for (long i=0; i<len ; i++){
            for (int j=0; j<days.size(); j++){
                if (j == i){
                    for (EventModel event: days.get((int)i).events) {
                        newDays.get((int)i).events.add(event);
                    }
                }

            }
        }
        this.days.clear();
        this.days = newDays;
        this.daysDropdown = new String[(int)res];
        this.dayToInt = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date curDate=startDate;
        for (int i=0;i<res;i++){
            String dayStr = dateFormat.format(curDate);
            daysDropdown[i]=dayStr;
            dayToInt.put(dayStr,i);
            days.get(i).dayString=dayStr;
            curDate = addDays(curDate, 1);
        }

    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public EventModel getEventById(String id){
        for (DayModel day : this.days)
        {
            for (EventModel event: day.events) {
                if (event.id.equals(id))
                {
                    return event;
                }
            }
        }
        return null;
    }

    public ArrayList<EventModel> getEvents(){
        ArrayList<EventModel> events = new  ArrayList<>();
        for (DayModel day : this.days)
        {
            events.addAll(day.events);
        }
        return events;
    }
    public EventModel getEventByName(String name){
        for (DayModel day : this.days)
        {
            for (EventModel event: day.events) {
                if (event.name.equals(name))
                {
                    return event;
                }
            }
        }
        return null;
    }
    public boolean isNameTaken(EventModel eventToCheck){
        for (EventModel event: days.get(eventToCheck.day).events) {
            if (event.name.equals(eventToCheck.name) && !event.id.equals(eventToCheck.id))
            {
                return true;
            }
        }
        return false;
    }
}
