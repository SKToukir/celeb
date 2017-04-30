package com.vumobile.notification;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toukirul on 27/4/2017.
 */

public class NetworkedService extends Service {
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

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.URL_ONLINE_USERS, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("FromServer", jsonObject.toString());

                    try {
                        JSONArray array = jsonObject.getJSONArray("result");

//
//                    } catch (JSONException e) {
//                        e.printStackTrace();


//                    }
//


                        } catch (JSONException e1) {
                        e1.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("FromServer", volleyError.toString());
                    Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();

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
}
