package com.vumobile.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.vumobile.celeb.AGApplication;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.ui.BaseActivity;
import com.vumobile.fan.login.ViaLive;
import com.vumobile.fan.login.ui.FanNotificationActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import io.agora.rtc.Constants;

/**
 * Created by toukirul on 27/4/2017.
 */

public class Utils extends BaseActivity {


    //    @RequiresApi(api = Build.VERSION_CODES.N)
//    @SuppressWarnings("static-access")
    public static void setCustomViewNotification(Context context, String name, String msisdn, String sample_url) {

        ((AGApplication) context).initWorkerThread();

        //contentDownloadActivity.doAction=1;
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
//        String strDate = sdf.format(c.getTime());

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an explicit intent for an ResultActivity to receive.

        Intent resultIntent;
        resultIntent = new Intent(context, ViaLive.class); //FanCelebProfileActivity


        resultIntent.putExtra("fbname", name);
        resultIntent.putExtra("msisdn", msisdn);
        resultIntent.putExtra("profilePic", sample_url);
        resultIntent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
        resultIntent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, name);
        resultIntent.putExtra("user", "fan");

        resultIntent.putExtra("CELEB_FB_NAME", name);

        Log.d("fbname", name);

     /*   DownloadTask downloadTask = new DownloadTask();

        *//** Starting the task created above *//*
        downloadTask.execute(Image);*/
        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ViaLive.class);


        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.push_activity);

        Intent volume = new Intent(context, ViaLive.class);//NotifActivityHandler // FanCelebProfileActivity

        volume.putExtra("DO", "2");
        PendingIntent pVolume = PendingIntent.getActivity(context, 1, resultIntent, 0);
        expandedView.setOnClickPendingIntent(R.id.MainlayoutCustom, pVolume);
        expandedView.setTextViewText(R.id.text_view, name);

        //expandedView.setTextViewText(R.id.notificationTime, strDate);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;

                try {
                    bitmap = Glide.with(context)
                            .load(sample_url)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                expandedView.setImageViewBitmap(R.id.imageViewTest, bitmap);


                // expandedView.setImageViewBitmap(R.id.imageViewTest, getBitmapFromURL(sample_url));
                //   expandedView.setImageViewResource(R.id.imageViewTest, R.mipmap.ic_launcher);


                Notification notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(getNotificationIcon())
                        .setLargeIcon(bitmap)//R.mipmap.ic_launcher
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent)
                        .setContentTitle(name + " Is live now")

                        //  .setDeleteIntent(pendintIntent)
                        .build();

                notification.bigContentView = expandedView;

                notification.defaults |= Notification.DEFAULT_SOUND;
                mNotificationManager.notify(0, notification);


            }
        }).start();


    }

    //    @RequiresApi(api = Build.VERSION_CODES.N)
    //   @SuppressWarnings("static-access")
    public static void setCustomViewPostNotification(Context context, String name, String celeb_image_url, String post_url, String comment, String like, String flags_notific, String gender, String msisdn, String celeb_id, String isImage) {


        //contentDownloadActivity.doAction=1;
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
//        String strDate = sdf.format(c.getTime());

        NotificationManager mNotificationManagerPost = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an explicit intent for an ResultActivity to receive.

        Intent resultIntent;
        resultIntent = new Intent(context, FanNotificationActivity.class);


        resultIntent.putExtra("fbname", name);
        resultIntent.putExtra("msisdn", msisdn);
        resultIntent.putExtra("post_url", post_url);
        resultIntent.putExtra("celeb_image_url", celeb_image_url);
        resultIntent.putExtra("comment", comment);
        resultIntent.putExtra("like", like);

        resultIntent.putExtra("flags_notific", flags_notific);
        resultIntent.putExtra("gender", gender);
        resultIntent.putExtra("celeb_id", celeb_id);
        resultIntent.putExtra("isImage", isImage);


        Log.d("fbname", name);

     /*   DownloadTask downloadTask = new DownloadTask();

        *//** Starting the task created above *//*
        downloadTask.execute(Image);*/
        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FanNotificationActivity.class);


        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.push_activity);

        Intent volume = new Intent(context, FanNotificationActivity.class);//NotifActivityHandler


        volume.putExtra("DO", "2");
        PendingIntent pVolume = PendingIntent.getActivity(context, 1, resultIntent, 0);
        expandedView.setOnClickPendingIntent(R.id.MainlayoutCustom, pVolume);
        expandedView.setTextViewText(R.id.text_view, name);

        //expandedView.setTextViewText(R.id.notificationTime, strDate);
//        try {
//            //expandedView.setImageViewBitmap(R.id.imageViewTest, remote_picture);
//            //expandedView.setImageViewResource(R.id.imageViewTest,R.mipmap.ic_launcher);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = null;

                try {
                    bitmap = Glide.with(context)
                            .load(celeb_image_url)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                expandedView.setImageViewBitmap(R.id.imageViewTest, bitmap);

                Notification notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(getNotificationIcon())
                        .setLargeIcon(bitmap)
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent)
                        .setContentTitle(name)
                        .setContentText(comment)

                        //  .setDeleteIntent(pendintIntent)
                        .build();

                notification.bigContentView = expandedView;

                notification.defaults |= Notification.DEFAULT_SOUND;
                mNotificationManagerPost.notify(0, notification);

            }
        }).start();


    }


    private static int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }
}