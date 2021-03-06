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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.ImageOrVideoView;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.adapter.FanCelebImageRecyclerViewAdapter;
import com.vumobile.fan.login.adapter.FanCelebVideoRecyclerViewAdapter;
import com.vumobile.fan.login.model.FanCelebImageModelEntity;
import com.vumobile.fan.login.model.FanCelebVideoModelEntity;
import com.vumobile.utils.MyInternetCheckReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FanCelebProfileImageVideo extends AppCompatActivity {

    private FanCelebImageRecyclerViewAdapter fanCelebImageRecyclerViewAdapter;
    private FanCelebVideoRecyclerViewAdapter fanCelebVideoRecyclerViewAdapter;
    private FanCelebImageModelEntity fanCelebImageModelEntity;
    private FanCelebVideoModelEntity fanCelebVideoModelEntity;
    private List<FanCelebImageModelEntity> fanCelebImageModelEntities;
    private List<FanCelebVideoModelEntity> fanCelebVideoModelEntities;
    private RecyclerView recyclerViewCelebImages, recyclerViewCelebVideos;
    private SwipeRefreshLayout swipeRefreshLayoutCelebImages, swipeRefreshLayoutCelebVideos;
    private String msisdn;

    LinearLayout activity_fan_gallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_celeb_profile_image_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_fan_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Gallery");
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Implemented by activity
        });

        // swipe to refresh
        swipeRefreshLayoutCelebImages = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutCelebImages);
        swipeRefreshLayoutCelebVideos = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutCelebVideos);
        swipeRefreshLayoutCelebImages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCelebImages(Api.URL_CELEB_POSTS, msisdn);
            }
        });
        swipeRefreshLayoutCelebVideos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCelebVideos(Api.URL_CELEB_POSTS, msisdn);
            }
        });
        swipeRefreshLayoutCelebImages.post(() -> {
            fetchCelebImages(Api.URL_CELEB_POSTS, msisdn);
        });
        swipeRefreshLayoutCelebVideos.post(() -> {
            fetchCelebVideos(Api.URL_CELEB_POSTS, msisdn);
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

        activity_fan_gallery = (LinearLayout) findViewById(R.id.activity_fan_gallery);

        // msisdn form previous activity
        boolean isCeleb = Session.isCeleb(getApplicationContext(), Session.IS_CELEB);

        if (isCeleb) {
            msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
            Log.d("ttt msisdn", "onCreate: " + msisdn);
        } else {
            Intent intent = getIntent();
            msisdn = intent.getExtras().getString("MSISDN");
            Log.d("ttt msisdn", "onCreate: " + msisdn);
        }

        // Set up the RecyclerView for image
        int numberOfColumnsImg = 3;
        int numberOfColumnsVid = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumnsImg);
        GridLayoutManager gridLayoutManagerV = new GridLayoutManager(this, numberOfColumnsVid);
        recyclerViewCelebImages.setLayoutManager(gridLayoutManager);
        recyclerViewCelebVideos.setLayoutManager(gridLayoutManagerV);

        fanCelebImageRecyclerViewAdapter = new FanCelebImageRecyclerViewAdapter(FanCelebProfileImageVideo.this, fanCelebImageModelEntities);
        fanCelebVideoRecyclerViewAdapter = new FanCelebVideoRecyclerViewAdapter(FanCelebProfileImageVideo.this, fanCelebVideoModelEntities);

        fanCelebImageRecyclerViewAdapter.setClickListener((view, position) -> {
            Intent intent = new Intent(this, ImageOrVideoView.class);
            intent.putExtra("IMG_OR_VID", "1");
            intent.putExtra("IMG_OR_VID_URL", fanCelebImageModelEntities.get(position).getImageUrl());//view.findViewById(R.id.imageViewRecyclerItem).getTag().toString())
            startActivity(intent);
        });

        fanCelebVideoRecyclerViewAdapter.setClickListener((view, position) -> {
            Intent intent = new Intent(this, ImageOrVideoView.class);
            intent.putExtra("IMG_OR_VID", "2");
            intent.putExtra("IMG_OR_VID_URL", fanCelebVideoModelEntities.get(position).getVideoUrl());
            startActivity(intent);
        });

        // show snackbar while no internet
        MyInternetCheckReceiver.isNetworkAvailableShowSnackbar(this, activity_fan_gallery);
        new MyInternetCheckReceiver(activity_fan_gallery);

    } // end of OnCreate


    private void fetchCelebImages(String celebImagesUrl, String celebMsisdn) {
        swipeRefreshLayoutCelebImages.setRefreshing(true);
        fanCelebImageModelEntities.clear();
        // String fullUrl = celebImagesUrl + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), celebMsisdn);
        String fullUrl = celebImagesUrl + "&MSISDN=" + celebMsisdn;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer celeb images", jsonObject.toString());

                try {

                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        fanCelebImageModelEntity = new FanCelebImageModelEntity();

                        String isImage = obj.getString("IsImage");
                        Log.d("ttt", isImage);
                        if (isImage.matches("1") || isImage.equals("1")) {
                            fanCelebImageModelEntity.setIsImage(obj.getString("IsImage"));
                            JSONArray posts = obj.getJSONArray("Post_Urls");

                            String imageUrl = posts.getString(0).trim();

                            if (imageUrl.length() > 5) {
                                fanCelebImageModelEntity.setImageUrl(imageUrl);
                            } else {
                                continue;
                            }

                        } else {
                            continue;
                        }

                        fanCelebImageModelEntities.add(fanCelebImageModelEntity);

                        recyclerViewCelebImages.setAdapter(fanCelebImageRecyclerViewAdapter);
                        fanCelebImageRecyclerViewAdapter.notifyDataSetChanged();

                    }

                    try {
                        Log.d("ttt list", "onResponse: " + fanCelebImageModelEntities.get(0).getImageUrl());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


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
        // Adding request to the queue
        Volley.newRequestQueue(FanCelebProfileImageVideo.this).add(request);

    }

    private void fetchCelebVideos(String celebImagesUrl, String celebMsisdn) {
        swipeRefreshLayoutCelebVideos.setRefreshing(true);
        fanCelebVideoModelEntities.clear();
        // String fullUrl = celebImagesUrl + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), celebMsisdn);
        String fullUrl = celebImagesUrl + "&MSISDN=" + celebMsisdn;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer celeb images", jsonObject.toString());

                try {

                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        fanCelebVideoModelEntity = new FanCelebVideoModelEntity();

                        String isImage = obj.getString("IsImage");
                        Log.d("ttt", isImage);
                        if (isImage.matches("2") || isImage.equals("2")) {
                            fanCelebVideoModelEntity.setSetIsImage(obj.getString("IsImage"));
                            Log.d("thumb", "onResponse: "+obj.getString("VideoThumb"));
                            JSONArray posts = obj.getJSONArray("Post_Urls");
                            JSONArray vThumbs = obj.getJSONArray("VideoThumb");

                            String imageUrl = posts.getString(0).trim();
                            String thumbUrl = vThumbs.getString(0).trim();
                            Log.d("thumb 2", vThumbs.toString());
                            Log.d("thumb 3", thumbUrl);
                            if (imageUrl.length() > 5) {
                                fanCelebVideoModelEntity.setVideoUrl(imageUrl);
                                fanCelebVideoModelEntity.setVideoThumbnail(thumbUrl);
                            } else {
                                continue;
                            }

                        } else {
                            continue;
                        }

                        fanCelebVideoModelEntities.add(fanCelebVideoModelEntity);

                        recyclerViewCelebVideos.setAdapter(fanCelebVideoRecyclerViewAdapter);
                        fanCelebVideoRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    //Log.d("ttt list", "onResponse: " + fanCelebVideoModelEntities.get(0).getVideoUrl());

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
