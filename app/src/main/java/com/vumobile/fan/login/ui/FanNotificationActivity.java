package com.vumobile.fan.login.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.adapter.FanNotificationAdapter;
import com.vumobile.fan.login.model.FanNotificationModelEnity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FanNotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerViewNotification;
    SwipeRefreshLayout swipeRefreshLayoutNotification;
    FanNotificationModelEnity fanNotificationModelEnity;
    List<FanNotificationModelEnity> fanNotificationModelEnities;
    FanNotificationAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        swipeRefreshLayoutNotification = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutNotification);
        recyclerViewNotification = (RecyclerView) findViewById(R.id.recyclerViewNotification);

//        FanNotificationModelEnity notificationModelEnity = new FanNotificationModelEnity();
//        notificationModelEnity.setName("Touhid");
//        notificationModelEnity.setLikeCount("12 " + "K");
//        notificationModelEnity.setMessage("Hello notification");
//        notificationModelEnity.setNotificationImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGMGFWVJIqODvtTaOp7W-qTnRuH0EFnlJ7OhuxGxVESU-L7oTnyury-xbM");
//        notificationModelEnity.setProfileImageUrl("http://www.leytonorient.com/images/common/bg_player_profile_default_big.png");
//        notificationModelEnity.setTime("12:11 PM");
//
//        FanNotificationModelEnity notificationModelEnity2 = new FanNotificationModelEnity();
//        notificationModelEnity2.setName("Touhid");
//        notificationModelEnity2.setLikeCount("12 " + "K");
//        notificationModelEnity2.setMessage("Hello notification");
//        notificationModelEnity2.setNotificationImageUrl("http://wallpaper-gallery.net/images/image-wallpaper/image-wallpaper-13.jpg");
//        notificationModelEnity2.setProfileImageUrl("http://www.leytonorient.com/images/common/bg_player_profile_default_big.png");
//        notificationModelEnity2.setTime("12:11 PM");


        fanNotificationModelEnities = new ArrayList<>();
        mAdapter = new FanNotificationAdapter(FanNotificationActivity.this, fanNotificationModelEnities);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNotification.setLayoutManager(mLayoutManager);
        recyclerViewNotification.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayoutNotification.setOnRefreshListener(FanNotificationActivity.this);
        swipeRefreshLayoutNotification.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    swipeRefreshLayoutNotification.setRefreshing(true);

                                                    fetchAllNotification();
                                                }
                                            }
        );


    } // end of onCreate

    @Override
    public void onRefresh() {
        fetchAllNotification();
    }

    private void fetchAllNotification() {

        swipeRefreshLayoutNotification.setRefreshing(false);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.URL_GET_ALL_NOTIFICATION_LIST, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer 12", jsonObject.toString());

                try {
                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        fanNotificationModelEnity = new FanNotificationModelEnity();
                        fanNotificationModelEnity.setName(obj.getString(Api.NOTIF_CELEB_NAME));
                        fanNotificationModelEnity.setTime(obj.getString(Api.NOTIF_TIME));
                        fanNotificationModelEnity.setProfileImageUrl(obj.getString(Api.NOTIF_CELEB_PIC_URL));

                        fanNotificationModelEnities.add(fanNotificationModelEnity);

                        recyclerViewNotification.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayoutNotification.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayoutNotification.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(FanNotificationActivity.this);

        //Adding request to the queue
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_celeb) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}