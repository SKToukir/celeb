package com.vumobile.notification;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Utils.Methods;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toukirul on 27/4/2017.
 */

public class NetworkedService extends Service {
    private boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("Tag", "Service onStartCommand");

        if (intent == null) {
            cleanupAndStopServiceRightAway();
            return START_NOT_STICKY;
        }
        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.


                try {

                    Thread.sleep(6000);

                    try {
                        new GetNotific().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {

                }

                //i = i+1;


            }
        }).start();

        return START_STICKY;

    }

    private void cleanupAndStopServiceRightAway() {
        // Add your code here to cleanup the service

        // Add your code to perform any recovery required
        // for recovering from your previous crash

        // Request to stop the service right away at the end
        stopSelf();
    }

    private class GetNotific extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Appps", "called receiver method");
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {

            String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
            String url = Api.URL_ONLINE_USERS + msisdn + Api.URL_ONLINE_KEY;
            Log.d("FromServer", url);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @SuppressWarnings("NewApi")
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("FromServerrr All data", jsonObject.toString());

                    if (jsonObject != null) {
                        try {
                            JSONArray array = jsonObject.getJSONArray("result");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                Log.d("FromServer 1", object.toString());
                                String notificationFlag = object.getString(Api.NOTIFICATION_FLAGS);

                                if (notificationFlag.equals("2")) { // || notificationFlag.matches("1") //  // flag 1 is live 2 is post

                                    TastyToast.makeText(getApplicationContext(), "This is Post notification!", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                    Log.d("FromServerrrPOST", "POST Notific");

                                    // save notification counter to shared pref
                                    if (array.length() > 0) {
                                        Session.setNotifShowCounter(getApplicationContext(), array.length());
                                    }

                                    for (int j = 0; j < array.length(); j++) {

                                        JSONObject object1 = array.getJSONObject(i);
                                        String name = object1.getString("name");
                                        Log.d("post", name);
                                        String celeb_image_url = object1.getString("Image_url");
                                        Log.d("post", celeb_image_url);
                                        JSONArray post_array = object1.getJSONArray("Post_Urls");
                                        String post_url = post_array.getString(0);
                                        Log.d("post", post_url);
                                        String comment = object1.getString("post");
                                        Log.d("post", comment);
                                        String like = object1.getString("likeCount");
                                        Log.d("post", like);
                                        String flags_notific = object1.getString("Flags_Notificaton");
                                        Log.d("post", flags_notific);
                                        String gender = object1.getString("gender");
                                        Log.d("post", gender);
                                        String msisdn = object1.getString("MSISDN");
                                        Log.d("post", msisdn);
                                        String isImage = object1.getString("IsImage");
                                        String celeb_id = object1.getString("Celeb_id");
                                        Log.d("post", celeb_id);

                                        Utils.setCustomViewPostNotification(getApplicationContext(), name, celeb_image_url, post_url, comment, like, flags_notific, gender, msisdn, celeb_id, isImage);

                                    }
                                } else {

                                    Log.d("FromServerrrLIVE", "LIVE Notific");

                                    String name = object.getString(Api.CELEB_NAME_NOTIFICATION);
                                    String msisdn = object.getString(Api.CELEB_MSISDN_NOTIFICATION);
                                    String profilePic = object.getString(Api.CELEB_IMAGE_URL_NOTIFICATION);
                                    String gender = object.getString(Api.CELEB_GENDER_NOTIFICATION);
                                    String celeb_id = object.getString(Api.CELEB_ID_NOTIFICATION);
                                    String currentTime = new Methods().getDate();

                                    // save notification data to server
                                    saveNotification(name, msisdn, profilePic, gender, celeb_id, currentTime);

                                    Utils.setCustomViewNotification(getApplicationContext(), name, msisdn, profilePic);

                                }

                            }


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("FromServer", volleyError.toString());
//                    Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(NetworkedService.this);

            //Adding request to the queue
            requestQueue.add(request);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("Response onPostExecute ", "> " + "onPostExecute");
        }

    }


    private void saveNotification(String name, String msisdn, String profilePicture, String gender, String celeb_id, String currentTime) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_SAVE_NOTIFICATION_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
                        try {
                            TastyToast.makeText(getApplicationContext(), response.toString(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer", "" + error.getMessage());
                        TastyToast.makeText(getApplicationContext(), "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Name", name);
                params.put("MSISDN", msisdn);
                params.put("celeb_id", celeb_id);
                params.put("gender", gender);
                params.put("Image_url", profilePicture);
                //params.put("time", currentTime);
//                params.put("Flag", String.valueOf(celebOrNot));


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onDestroy() {
        isRunning = false;
        sendBroadcast(new Intent("YouWillNeverKillMe"));
        Log.i("TAG", "Service onDestroy");
    }
}
