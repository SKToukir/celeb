package com.vumobile.celeb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.GiftsAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.GiftClass;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    GiftClass giftClass;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<GiftClass> giftClassList = new ArrayList<GiftClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        toolbar = (Toolbar) findViewById(R.id.toolBar_post_gifts);
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

        fetchData();
    }

    private void fetchData() {

        String msisdn = Session.retreivePhone(getApplicationContext(),Session.USER_PHONE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.API_GET_GIFTS + msisdn, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("FromServer",response.toString());

                try {
                    JSONArray array = response.getJSONArray("result");

                    for (int i = 0; i < array.length(); i++){

                        giftClass = new GiftClass();
                        JSONObject object = array.getJSONObject(i);
                        giftClass.setImageUrl(object.getString("Image_url"));
                        Log.d("FromServer",giftClass.getImageUrl());

                        JSONArray array1 = object.getJSONArray("Post_Urls");

                        giftClass.setListOfGift(array1);

                        giftClassList.add(giftClass);

                    }

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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
    }

    private void initUI() {
        adapter = new GiftsAdapter(getApplicationContext(),giftClassList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerGifts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



    }
}
