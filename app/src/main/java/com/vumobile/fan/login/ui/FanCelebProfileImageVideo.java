package com.vumobile.fan.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.adapter.FanCelebImageRecyclerViewAdapter;
import com.vumobile.fan.login.adapter.FanCelebVideoRecyclerViewAdapter;
import com.vumobile.fan.login.model.FanCelebImageModelEntity;
import com.vumobile.fan.login.model.FanCelebVideoModelEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FanCelebProfileImageVideo extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, FanCelebImageRecyclerViewAdapter.ItemClickListener, FanCelebVideoRecyclerViewAdapter.ItemClickListener {

    private FanCelebImageRecyclerViewAdapter fanCelebImageRecyclerViewAdapter;
    private FanCelebVideoRecyclerViewAdapter fanCelebVideoRecyclerViewAdapter;
    private FanCelebImageModelEntity fanCelebImageModelEntity;
    private FanCelebVideoModelEntity fanCelebVideoModelEntity;
    private List<FanCelebImageModelEntity> fanCelebImageModelEntities;
    private List<FanCelebVideoModelEntity> fanCelebVideoModelEntities;
    private RecyclerView recyclerViewCelebImages, recyclerViewCelebVideos;
    private SwipeRefreshLayout swipeRefreshLayoutCelebImages, swipeRefreshLayoutCelebVideos;
    private String msisdn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_celeb_profile_image_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_fan_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Implemented by activity
        });

        // swipe to refresh
        swipeRefreshLayoutCelebImages = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutCelebImages);
        swipeRefreshLayoutCelebVideos = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutCelebVideos);
        swipeRefreshLayoutCelebImages.setOnRefreshListener(this);
        swipeRefreshLayoutCelebVideos.setOnRefreshListener(this);
        swipeRefreshLayoutCelebImages.post(() -> {
         //   fetchCelebImages(Api.URL_GET_CELEB_PROFILE, ); TODO touhid
        });
        swipeRefreshLayoutCelebVideos.post(() -> {

        });

        // Data models
        fanCelebImageModelEntity = new FanCelebImageModelEntity();
        fanCelebVideoModelEntity = new FanCelebVideoModelEntity();
        fanCelebImageModelEntities = new ArrayList<>();
        fanCelebVideoModelEntities = new ArrayList<>();

        recyclerViewCelebImages = (RecyclerView) findViewById(R.id.recyclerViewCelebImages);
        recyclerViewCelebVideos = (RecyclerView) findViewById(R.id.recyclerViewCelebVideos);
        recyclerViewCelebImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCelebVideos.setItemAnimator(new DefaultItemAnimator());

        // msisdn form previous activity
        Intent intent = getIntent();
        msisdn = intent.getExtras().getString("MSISDN");
        Log.d("ttt msisdn", "onCreate: " + msisdn);

        // Set up the RecyclerView for image
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        GridLayoutManager gridLayoutManagerV = new GridLayoutManager(this, numberOfColumns);
        recyclerViewCelebImages.setLayoutManager(gridLayoutManager);
        recyclerViewCelebVideos.setLayoutManager(gridLayoutManagerV);
        fetchCelebImages(Api.URL_ACTIVATE_USERS, msisdn);
        fetchCelebVideos(Api.URL_ACTIVATE_USERS, msisdn);
        fanCelebImageRecyclerViewAdapter = new FanCelebImageRecyclerViewAdapter(FanCelebProfileImageVideo.this, fanCelebImageModelEntities);
        fanCelebVideoRecyclerViewAdapter = new FanCelebVideoRecyclerViewAdapter(FanCelebProfileImageVideo.this, fanCelebVideoModelEntities);
        fanCelebImageRecyclerViewAdapter.setClickListener(this);
        fanCelebVideoRecyclerViewAdapter.setClickListener(this);


    } // end of OnCreate

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.recyclerViewCelebImages:

                Toast.makeText(this, "I " + position, Toast.LENGTH_SHORT).show();

                break;

            case R.id.recyclerViewCelebVideos:

                Toast.makeText(this, "V " + position, Toast.LENGTH_SHORT).show();

                break;

        }
    }

    @Override
    public void onRefresh() {
        fetchCelebImages(Api.URL_ACTIVATE_USERS, msisdn);
    }

    private void fetchCelebImages(String celebImagesUrl, String celebMsisdn) {
        swipeRefreshLayoutCelebImages.setRefreshing(true);
        fanCelebImageModelEntities.clear();
        // String fullUrl = celebImagesUrl + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), celebMsisdn);
        String fullUrl = celebImagesUrl + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer celeb images", jsonObject.toString());

                try {

                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        fanCelebImageModelEntity = new FanCelebImageModelEntity();

                        fanCelebImageModelEntity.setImageUrl(obj.getString("Image_url"));

                        fanCelebImageModelEntities.add(fanCelebImageModelEntity);

                        recyclerViewCelebImages.setAdapter(fanCelebImageRecyclerViewAdapter);
                        fanCelebImageRecyclerViewAdapter.notifyDataSetChanged();

                    }

                    Log.d("ttt list", "onResponse: " + fanCelebImageModelEntities.get(0).getImageUrl());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayoutCelebImages.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayoutCelebImages.setRefreshing(false);
            }
        });
        //Adding request to the queue
        Volley.newRequestQueue(FanCelebProfileImageVideo.this).add(request);

    }

    private void fetchCelebVideos(String celebImagesUrl, String celebMsisdn) {
        swipeRefreshLayoutCelebImages.setRefreshing(true);
        fanCelebImageModelEntities.clear();
        // String fullUrl = celebImagesUrl + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), celebMsisdn);
        String fullUrl = celebImagesUrl + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer celeb images", jsonObject.toString());

                try {

                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        fanCelebVideoModelEntity = new FanCelebVideoModelEntity();

                        fanCelebVideoModelEntity.setVideoUrl(obj.getString("Image_url"));

                        fanCelebVideoModelEntities.add(fanCelebVideoModelEntity);

                        recyclerViewCelebVideos.setAdapter(fanCelebVideoRecyclerViewAdapter);
                        fanCelebVideoRecyclerViewAdapter.notifyDataSetChanged();

                    }

                    Log.d("ttt list", "onResponse: " + fanCelebVideoModelEntities.get(0).getVideoUrl());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayoutCelebVideos.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayoutCelebVideos.setRefreshing(false);
            }
        });
        //Adding request to the queue
        Volley.newRequestQueue(FanCelebProfileImageVideo.this).add(request);

    }


}
