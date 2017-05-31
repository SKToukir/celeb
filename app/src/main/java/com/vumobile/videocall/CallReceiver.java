package com.vumobile.videocall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by toukirul on 21/5/2017.
 */

public class CallReceiver extends BroadcastReceiver {
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Appp", "called receiver method");
        try{

            //Utils.generateNotification(context);
            // Utils.sendNotification(context,"Amar","Sticker");
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent serviceIntent = new Intent(context, SinchService.class);
                context.startService(serviceIntent);
                Intent myIntent = new Intent(context, CallReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60 * 1000, pendingIntent);
            }
            context.startService(new Intent(context.getApplicationContext(), SinchService.class));

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
//class u extends BaseActivity implements SinchService.StartFailedListener{
//
//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        RegisterVideoCall(this);
//    }
//
//    public void RegisterVideoCall(Context context){
//        if (!getSinchServiceInterface().isStarted()) {
//            SinchService.uName = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);
//            Log.d("sssssssss",Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME));
//            startService(new Intent(context, SinchService.class));
//            getSinchServiceInterface().startClient(Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME));
//
//        } else {
//            Intent intent = new Intent(context,SinchService.class);
//            SinchService.uName = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);
//            startService(intent);
//        }
//
//    }
//
//    @Override
//    protected void onServiceConnected() {
//        getSinchServiceInterface().setStartListener(this);
//    }
//
//    @Override
//    public void onStartFailed(SinchError error) {
//
//    }
//
//    @Override
//    public void onStarted() {
//
//    }
//}