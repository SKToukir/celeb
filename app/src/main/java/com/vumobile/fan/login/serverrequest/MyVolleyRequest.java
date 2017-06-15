package com.vumobile.fan.login.serverrequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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

    public static void getAllGenericDataJsonObject(Context context, int method, String url, AllVolleyInterfaces.MyJsonObjectRequest myJsonObjectRequest) {
        JsonObjectRequest request = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                myJsonObjectRequest.getResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                myJsonObjectRequest.getResponseErr(volleyError);
            }
        });
        Volley.newRequestQueue(context).add(request);
    }

    public static void sendAllGenericDataJsonObject(Context context, int method, String url, HashMap<String, String> params,
                                                    AllVolleyInterfaces.ResponseString responseString) {

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public static void setRegId(Context context, int method, String url, HashMap<String, String> params, AllVolleyInterfaces.ResponseString responseString) {

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }


}
