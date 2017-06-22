package com.vumobile.celeb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.RequestAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.RequestClass;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    String request_type;
    private List<RequestClass> requestClasses = new ArrayList<>();
    private ListView listOfRequests;
    private RequestAdapter adapter;
    RequestClass requestClass;

    private Toolbar toolbar;
    private ImageView imgBackRequest;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        toolbar = (Toolbar) findViewById(R.id.toolbar_request);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        initUI();

        adapter = new RequestAdapter(getApplicationContext(), R.layout.request_row, requestClasses);
        listOfRequests.setAdapter(adapter);

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        retreiveRequest(msisdn);
    }

    private void retreiveRequest(String msisdn) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Api.URL_FAN_REQUESTS + msisdn, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("request",response.toString());
                try {
                    JSONArray array = response.getJSONArray("result");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        requestClass = new RequestClass();
                        requestClass.setFanName(obj.getString("Name"));
                        Log.d("dataa",obj.getString("Name"));
                        requestClass.setImageUrl(obj.getString("Image_url"));
                        Log.d("dataa",obj.getString("Image_url"));
                        request_type = obj.getString("RequestType");
                        requestClass.setRequest_type(request_type);
                        Log.d("dataa",request_type);
                        if (request_type.matches("1")){
                            requestClass.setRequest("Request for chat");

                        }else {
                            requestClass.setRequest("Request for video");
                        }

                        String request_time = convertTimeStamp(obj.getString("RequestTime"));
                        Log.d("time",request_time);
                        requestClass.setRequestToTime(request_time);
                        requestClass.setMSISDN(obj.getString("MSISDN"));
                        Log.d("dataa",request_time);

                        requestClasses.add(requestClass);
                        listOfRequests.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

//        for (int i = 0; i <=5; i++){
//
//            requestClass = new RequestClass();
//            requestClass.setFanName("Tonmoy Sheikh");
//            requestClass.setImageUrl("https://graph.facebook.com/1931218820457638/picture?width=500&height=500");
//            requestClass.setRequestToTime("Request for chat");
//            requestClass.setRequestTime("9/05/2017 5.30pm");
//
//            requestClasses.add(requestClass);
//            listOfRequests.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
//


    }

    private String convertTimeStamp(String requestTime) {
        String strDate = requestTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(date);

        return date.toString();
    }

    private void initUI() {

        listOfRequests = (ListView) findViewById(R.id.listOfRequests);
        imgBackRequest = (ImageView) toolbar.findViewById(R.id.backCelebRequest);
        imgBackRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.backCelebRequest:
                intent = new Intent(RequestActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
