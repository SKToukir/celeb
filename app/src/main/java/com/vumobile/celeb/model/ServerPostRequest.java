package com.vumobile.celeb.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toukirul on 27/4/2017.
 */

public class ServerPostRequest {

    public void onLive(Context mContext,String msisdn,String liveStatus){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_POST_LIVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer",""+error.getMessage());

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("MSISDN",msisdn);
                params.put("live",liveStatus);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }
}
