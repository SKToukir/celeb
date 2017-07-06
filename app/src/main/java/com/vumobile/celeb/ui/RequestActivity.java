package com.vumobile.celeb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.android.volley.toolbox.StringRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    String request_type;
    private List<RequestClass> requestClasses = new ArrayList<>();
    private ListView listOfRequests;
    private RequestAdapter adapter;
    RequestClass requestClass;

    private Toolbar toolbar;
    private ImageView imgBackRequest;
    private Intent intent;
    String msisdn;

    private SwipeRefreshLayout swipeRefreshLayout;


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

        msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        retreiveRequest(msisdn);

        removeBadge();

    }

    private void retreiveRequest(String msisdn) {

        swipeRefreshLayout.setRefreshing(true);
        requestClasses.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Api.URL_FAN_REQUESTS + msisdn, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("request", response.toString());
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONArray array = response.getJSONArray("result");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        requestClass = new RequestClass();
                        requestClass.setID(obj.getString("cid"));
                        requestClass.setFanName(obj.getString("Name"));
                        Log.d("dataa", obj.getString("Name"));
                        requestClass.setImageUrl(obj.getString("Image_url"));
                        Log.d("dataa", obj.getString("Image_url"));
                        request_type = obj.getString("RequestType");
                        requestClass.setRequest_type(request_type);
                        Log.d("dataa", request_type);
                        if (request_type.matches("1")) {
                            requestClass.setRequest("Request for chat");

                        } else {
                            requestClass.setRequest("Request for video");
                        }

                        String request_time = convertTimeStamp(obj.getString("RequestTime"));
                        Log.d("time", request_time);
                        requestClass.setRequestToTime(request_time);
                        requestClass.setMSISDN(obj.getString("MSISDN"));
                        Log.d("dataa", request_time);

                        requestClasses.add(requestClass);
                        listOfRequests.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }

                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Volley.newRequestQueue(this).add(request);


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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_req);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        retreiveRequest(msisdn);
                                    }
                                }
        );

    }

    @Override
    public void onRefresh() {
        retreiveRequest(msisdn);
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


    private void removeBadge() {

        String url = "http://wap.shabox.mobi/testwebapi/Celebrity/UpdateRequestCount?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
                        Log.d("FromServer", "" + error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MSISDN", msisdn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
