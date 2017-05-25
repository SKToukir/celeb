package com.vumobile.celeb.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.CelebPostAdapter;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.model.FanNotificationModelEnity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CelebEditPostActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayoutNotification;
    private RecyclerView.LayoutManager mLayoutManager;
    private CelebPostAdapter adapter;
    private RecyclerView recyclerView;
    private String msisdn;
    private List<FanNotificationModelEnity> entityList = new ArrayList<FanNotificationModelEnity>();
    private FanNotificationModelEnity enity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celeb_edit_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar_posts);
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


        msisdn = Session.retreivePhone(getApplicationContext(),Session.USER_PHONE);

        swipeRefreshLayoutNotification.setOnRefreshListener(CelebEditPostActivity.this);
        swipeRefreshLayoutNotification.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    swipeRefreshLayoutNotification.setRefreshing(true);
                                                    parseData();
                                                }
                                            }
        );


    } // end of onCreate

    private void parseData() {
        swipeRefreshLayoutNotification.setRefreshing(false);
        entityList.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.URL_CELEB_POSTS+"&MSISDN="+msisdn, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer12", jsonObject.toString());

                try {
                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        enity = new FanNotificationModelEnity();
                        enity.setId(obj.getString(Api.NOTIF_ID));
                        enity.setName(obj.getString(Api.NOTIF_CELEB_NAME));
                        enity.setMSISDN(obj.getString(Api.NOTIF_CELEB_MSISDN));
                        enity.setCeleb_id(obj.getString(Api.NOTIF_CELEB_ID));
                        enity.setGender(obj.getString(Api.NOTIF_CELEB_GENDER));
                        enity.setIsImage(obj.getString(Api.NOTIF_IS_IMAGE));
                        enity.setFlags_Notificaton(obj.getString(Api.NOTIF_FLAG_NOTIF));
                        enity.setImage_url(obj.getString(Api.NOTIF_CELEB_PIC_URL));
                        enity.setPost(obj.getString(Api.NOTIF_CELEB_POST));
                        enity.setTimeStamp(obj.getString(Api.NOTIF_TIME));
                        enity.setLikeCount(obj.getString(Api.NOTIF_LIKE_COUNT));
                        enity.setPost_Urls(obj.getString(Api.NOTIF_POST_URLS));


                        entityList.add(enity);

                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

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

        RequestQueue requestQueue = Volley.newRequestQueue(CelebEditPostActivity.this);

        //Adding request to the queue
        requestQueue.add(request);
    }

    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCelebEditPost);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        swipeRefreshLayoutNotification = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutCelebPost);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CelebPostAdapter(getApplicationContext(),entityList);

    }

    @Override
    public void onRefresh() {
        parseData();
    }
}
