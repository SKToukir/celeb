package com.vumobile.fan.login.serverrequest;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class AllVolleyInterfaces {

    public interface ResponseString{
        void getResponse(String responseResult);
        void getResponseErr(String responseResultErr);
    }

    public interface MyJsonObjectRequest{
        void getResponse(JSONObject jsonObject);
        void getResponseErr(VolleyError volleyError);
    }

}
