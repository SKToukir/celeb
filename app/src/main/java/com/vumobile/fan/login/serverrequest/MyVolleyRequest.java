package com.vumobile.fan.login.serverrequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class MyVolleyRequest {

    public static void getAllGifts(Context context, int method, String url, AllVolleyInterfaces.ResponseString responseString) {

        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
                        responseString.getResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        responseString.getResponseErr(error.getMessage());
                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }

}
