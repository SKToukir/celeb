package com.vumobile.fan.login.serverrequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

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
    public static void getAllGenericDataString(Context context, int method, String url, AllVolleyInterfaces.ResponseString responseString) {
        StringRequest stringRequest = new StringRequest(method, url,
                response -> {
                    Log.d("FromServer AllGeneric", response.toString());
                    responseString.getResponse(response);
                },
                error -> {
                    error.printStackTrace();
                    responseString.getResponseErr(error.getMessage());
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public static void setRegId(Context context, int method, String url, HashMap<String,String> params, AllVolleyInterfaces.ResponseString responseString) {

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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }


}
