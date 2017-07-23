package com.vumobile.fan.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IT-10 on 7/17/2017.
 */

public class SmsResultReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        readSms(context);

    }

    public static void readSms(Context context) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);

                    String smsReadNo = "6624";

                    Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"), null, "address='" + smsReadNo + "'", null, null);
                    Log.d("ddeerr", "readSms: " + cursor + " cc:" + cursor.getCount());
                    if (cursor != null && cursor.getCount() != 0) { // must check the result to prevent exception
                        cursor.moveToFirst();
                        do {
                            String msgData = "";
                            try {
                                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                                    //    Log.d("data sms", "onReceive: " + cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                                    Log.d("tttt", "onReceive: " + idx + " " + cursor.getColumnName(idx) + " " + cursor.getString(idx));
                                    Log.d("ttt sms dd", "cursor : " + msgData);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                readSms(context); // if cursor error try again
                                break;
                            }

                            // Use msgData
                            String a = msgData;
                            String startWord = "Code is:";
                            String endWord = "service_center"; //service_center
                            String pattern = startWord + "(.*?)" + endWord;
                            Pattern r = Pattern.compile(pattern);
                            // Now create matcher object.
                            Matcher m = r.matcher(a);
                            if (m.find()) {
                                Log.d("see sms", "onReceive: " + m.group(1).replaceAll("\\s", ""));
                            }

                            LogInAcitvity.setVerifyPinCodeFromSms(m.group(1).replaceAll("\\s", ""));

                            break; // first time of loop will collect last item of sms if more than one sms from same smsReadNo
                        } while (cursor.moveToNext());

                    } else {
                        Log.d("Empty inbox ", "readSms: ");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }


}



