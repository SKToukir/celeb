package com.vumobile.alarm;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toukirul on 20/7/2017.
 */

public class AlarmTimeClass {

    public long getDateTimeInmilSec(String dateTime){
        String myDate = dateTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        Log.d("timeInmil",String.valueOf(millis));
        return millis;
    }

    public String timeFormat(String time) {

        String strCurrentDate = time;

        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy hh:mma");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = format.format(newDate);

        Log.d("dateyear", date);



        return String.valueOf(getDateTimeInmilSec(date));

    }

    public static long getCurrentTime(){
        long time= System.currentTimeMillis();
        Log.d("alarmTime","currentTime "+String.valueOf(time));
        return time;
    }

}
