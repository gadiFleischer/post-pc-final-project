package com.example.tripplanner.models;

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

public class TripModel implements Serializable {
    public String id;
    public String title;
    public String destination;
    public Date startDate;
    public Date endDate;
    public String countryCode;
    public ArrayList<DayModel> days;
    public  String[] daysDropdown;
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
            this.days.add(new DayModel(i+1,"red"));//TODO:think about color default
        }
        this.daysDropdown = new String[(int)res];
        this.dayToInt = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date curDate=startDate;
        for (int i=0;i<res;i++){
            String dayStr = dateFormat.format(curDate);
            daysDropdown[i]=dayStr;
            dayToInt.put(dayStr,i);
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
}
