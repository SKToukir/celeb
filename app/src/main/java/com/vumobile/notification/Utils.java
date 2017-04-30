package com.vumobile.notification;

/**
 * Created by toukirul on 27/4/2017.
 */

public class Utils {
//    private static NotificationManager mNotificationManager;
//
//    @SuppressWarnings("static-access")
//    public static void setCustomViewNotification(Context context, String sample_url, String contentCode, String image_title, String categoryCode, String sContentType, String sPhysicalFileName, String ZedID, String sms, int i) {
//
//        Bitmap remote_picture = null;
//
//        try {
//
//            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(sample_url).getContent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //contentDownloadActivity.doAction=1;
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
//        String strDate = sdf.format(c.getTime());
//
//        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        // Creates an explicit intent for an ResultActivity to receive.
//
//        Intent resultIntent;
//        resultIntent = new Intent(context,PictureDetailsActivity.class);
//        if (sContentType.matches("WP")||sContentType.matches("ST")) {
//            resultIntent = new Intent(context,PictureDetailsActivity.class);
//            PictureDetailsActivity.PIC_TYPE = "pic";
//            PictureDetailsActivity.related_pic = Config.URL_PICTURE;
//
//        } else if(sContentType.matches("FV")||sContentType.matches("VD")) {
//            resultIntent = new Intent(context,VideoPreViewActivity.class);
//            VideoPreViewActivity.URL_RELATED_VIDEO = Config.URL_FULL_VIDEO;
//            VideoPreViewActivity.TYPE = "fullVideo";
//        }
//        else if(sContentType.matches("JG")) {
//            resultIntent = new Intent(context,PictureDetailsActivity.class);
//            PictureDetailsActivity.related_pic = Config.URL_PICTURE;
//            PictureDetailsActivity.PIC_TYPE = "pic";
//        }
//        else if(sContentType.matches("BFT")) {
//            resultIntent = new Intent(context,PlaySongActivity.class);
//            PlaySongActivity.related_song = Config.URL_BANGLA_SONG;
//            PlaySongActivity.SONG_TYPE = "bangla";
//        }
//
//        resultIntent.putExtra("contentCode", contentCode);
//        resultIntent.putExtra("categoryCode", categoryCode);
//        resultIntent.putExtra("contentName", image_title);
//        resultIntent.putExtra("sContentType", sContentType);
//        resultIntent.putExtra("sPhysicalFileName", sPhysicalFileName);
//        resultIntent.putExtra("contentImg", sample_url);
//        resultIntent.putExtra("ZedID", ZedID);
//        resultIntent.putExtra("doAction",2);
//
//
//        Log.d("contentCode", contentCode);
//        Log.d("categoryCode", categoryCode);
//        Log.d("contentName", image_title);
//        Log.d("sContentType", sContentType);
//        Log.d("sPhysicalFileName", sPhysicalFileName);
//        Log.d("contentImg", sample_url);
//        Log.d("ZedID", ZedID);
//     /*   DownloadTask downloadTask = new DownloadTask();
//
//        *//** Starting the task created above *//*
//        downloadTask.execute(Image);*/
//        // This ensures that the back button follows the recommended convention for the back key.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity.class);
//
//
//
//        // Adds the Intent that starts the Activity to the top of the stack.
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Create remote view and set bigContentView.
//        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.push_activity);
//
//        Intent volume = new Intent(context, MainActivity.class);//NotifActivityHandler
//
//
//        volume.putExtra("DO", "2");
//        PendingIntent pVolume = PendingIntent.getActivity(context, 1, resultIntent, 0);
//        expandedView.setOnClickPendingIntent(R.id.MainlayoutCustom, pVolume);
//        expandedView.setTextViewText(R.id.text_view, sms);
//
//        //expandedView.setTextViewText(R.id.notificationTime, strDate);
//
//        try {
//            expandedView.setImageViewBitmap(R.id.imageViewTest, remote_picture );
//
//        }catch (Exception e){
//
//            e.printStackTrace();
//        }
//
//        Notification notification = new NotificationCompat.Builder(context)
//                .setSmallIcon(getNotificationIcon())
//                .setLargeIcon(remote_picture)
//                .setAutoCancel(true)
//                .setContentIntent(resultPendingIntent)
//                .setContentTitle(sms)
//
//                //  .setDeleteIntent(pendintIntent)
//                .build();
//
//        notification.bigContentView = expandedView;
//
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        mNotificationManager.notify(0, notification);
//    }
//
//
//
//    private static int getNotificationIcon() {
//        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return whiteIcon ? R.drawable.ic_eye : R.drawable.ic_eye;
//    }

}