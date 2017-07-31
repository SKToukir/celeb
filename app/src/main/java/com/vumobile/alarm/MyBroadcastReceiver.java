package com.vumobile.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.vumobile.ParentActivity;
import com.vumobile.celeb.R;
import com.vumobile.celeb.ui.CelebHomeActivity;
import com.vumobile.fan.login.Session;

/**
 * Created by toukirul on 20/7/2017.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    static MediaPlayer mp;
    static int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("alarmNotification", "alarm begin");

        setNotification(context);

        mp = MediaPlayer.create(context, R.raw.old);
        mp.start();



//        ArrayList<String> timeList = SharedPref.getList(context);


//        long currentTime, alarmTime;
//        alarmTime = Long.parseLong(timeList.get(0));
//        currentTime = AlarmTimeClass.getCurrentTime();

//        if (alarmTime >= currentTime){


//        if (timeList.size() > 0) {
//
//
//                SharedPref.clearListShared(context);
//                SharedPref.SaveList(context, timeList);
//                setAgai(context, Long.parseLong(timeList.get(i)));
//                Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
//
//        } else {
//            MyBroadcastReceiver.this.abortBroadcast();
//        }
        //}

    }

    public void setAgai(Context context, long time) {

        Log.d("timelist+1", String.valueOf(SharedPref.getList(context).get(i)));
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 234324243, intent, 0);


//        for (int u = 0; u < time.length; u++) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        Toast.makeText(context, "Alarm set in " + time + " seconds", Toast.LENGTH_LONG).show();
    }

    public void setNotification(Context context) {

        boolean isCeleb = Session.isCeleb(context,Session.IS_CELEB);



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);

//Create the intent that’ll fire when the user taps the notification//
        Intent intent;
        if (isCeleb){

            intent = new Intent(context, CelebHomeActivity.class);
        }else {
            intent = new Intent(context, ParentActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Celebrity");
        mBuilder.setContentText("Celebrity schedule alarm!");

        NotificationManager mNotificationManager =

                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());


    }
}