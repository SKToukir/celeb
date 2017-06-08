package com.vumobile.celeb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.ScheduleAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.CelebScheduleClass;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    public static String USER_TYPE;
    private Toolbar toolbar;
    private ImageView back;
    private Intent intent;
    CelebScheduleClass requestClass;
    List<CelebScheduleClass> listOfSchedule = new ArrayList<CelebScheduleClass>();
    ScheduleAdapter adapter;
    private ListView scheduleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = (Toolbar) findViewById(R.id.toolbar_schedule);
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

        getScheduleData(Api.URL_GET_SCHEDULES);

        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CelebScheduleClass data = listOfSchedule.get(i);
                String time = data.getStart_time();

            }
        });
    }

    private void getScheduleData(String urlGetSchedules) {

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetSchedules,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray array = object.getJSONArray("result");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject obj = array.getJSONObject(i);
                                requestClass = new CelebScheduleClass();
                                requestClass.setName(obj.getString("Name"));
                                Log.d("FromServer", requestClass.getName());
                                requestClass.setImageUrl(obj.getString("Image_url"));
                                Log.d("FromServer", requestClass.getImageUrl());
                                requestClass.setStart_time(obj.getString("StartTime"));
                                Log.d("FromServer", requestClass.getStart_time());
                                requestClass.setEnd_time(obj.getString("EndTime"));
                                Log.d("FromServer", requestClass.getEnd_time());

                                listOfSchedule.add(requestClass);

                                scheduleList.setAdapter(adapter);
                                Log.d("adapter", "onResponse: " + adapter.getCount());
                                adapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                /*
                *  request flag = 1 means it is a chat request
                *  request flag = 2 means it is a video request
                * */

                Map<String, String> params = new HashMap<String, String>();
                params.put("flag", USER_TYPE);
                params.put("MSISDN", msisdn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initUI() {

        back = (ImageView) toolbar.findViewById(R.id.backCelebSchedule);
        back.setOnClickListener(this);

        scheduleList = (ListView) findViewById(R.id.scheduleList);

        adapter = new ScheduleAdapter(getApplicationContext(), R.layout.row_schedule_layout, listOfSchedule);
        scheduleList.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.backCelebSchedule:
                intent = new Intent(ScheduleActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
