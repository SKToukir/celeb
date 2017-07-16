package com.vumobile.celeb.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.GiftsAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.Gift;
import com.vumobile.celeb.model.GiftClass;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private Button buttonFilterGiftAll, buttonFilterLive;
    private SwipeRefreshLayout swipeRefreshLayoutGift;
    private Toolbar toolbar;
    GiftClass giftClass;
    GiftsAdapter adapter;
    RecyclerView recyclerView;
    List<GiftClass> giftClassList = new ArrayList<>();
    List<Gift> giftList = new ArrayList<Gift>();

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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
//                JSONArray array = giftList.get(position).getArray();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void fetchData(String flag) {

        swipeRefreshLayoutGift.setRefreshing(true);
        giftClassList.clear();

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        StringRequest sr = new StringRequest(Request.Method.POST, Api.API_GET_GIFTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("FromServer", response.toString());

                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("result");

                    for (int i = 0; i < array.length(); i++) {

                        giftClass = new GiftClass();
                        JSONObject object = array.getJSONObject(i);
                        giftClass.setImageUrl(object.getString("Image_url"));
                        Log.d("FromServer", giftClass.getImageUrl());

                        JSONArray array1 = object.getJSONArray("Post_Urls");
                        Log.d("hjhjhjhjhjh", array1.toString());
                        giftClass.setArray(array1);
                        giftClass.setTotalGifts(String.valueOf(array1.length()));
                        Log.d("ListSize", String.valueOf(array1.length()));

                        giftClassList.add(giftClass);

                    }

                     recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayoutGift.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayoutGift.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MSISDN", msisdn);
                params.put("FLAG", flag); // 0 for all, 5 for only live gifts
                return params;
            }
        };

        Volley.newRequestQueue(this).add(sr);

    }

    private void initUI() {

        swipeRefreshLayoutGift = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_gift);
        swipeRefreshLayoutGift.setOnRefreshListener(this);

        buttonFilterGiftAll = (Button) findViewById(R.id.buttonFilterGiftAll);
        buttonFilterLive = (Button) findViewById(R.id.buttonFilterLive);

        buttonFilterGiftAll.setOnClickListener(this);
        buttonFilterGiftAll.setTag("SELECT_ITEM");
        buttonFilterLive.setOnClickListener(this);

        adapter = new GiftsAdapter(getApplicationContext(), giftClassList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerGifts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


        swipeRefreshLayoutGift.post(new Runnable() {
            @Override
            public void run() {
                if (buttonFilterGiftAll.getTag().equals("SELECT_ITEM")) {
                    fetchData("0");
                } else {
                    fetchData("5");
                }

            }
        });

    }

    @Override
    public void onRefresh() {
        if (buttonFilterGiftAll.getTag().equals("SELECT_ITEM")) {
            fetchData("0");
        } else {
            fetchData("5");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonFilterGiftAll:
                changeButtonSelectFocus(buttonFilterGiftAll);
                fetchData("0");
                break;

            case R.id.buttonFilterLive:
                changeButtonSelectFocus(buttonFilterLive);
                fetchData("5");
                break;

        }

    }

    private void changeButtonSelectFocus(Button button) {
        buttonFilterGiftAll.setBackground(getResources().getDrawable(R.drawable.button_border_selected_item));
        buttonFilterGiftAll.setTextColor(getResources().getColor(R.color.myColorTwoHeader));
        buttonFilterGiftAll.setTag("ITEM");
        buttonFilterLive.setBackground(getResources().getDrawable(R.drawable.button_border_selected_item));
        buttonFilterLive.setTextColor(getResources().getColor(R.color.myColorTwoHeader));
        buttonFilterLive.setTag("ITEM");

        button.setBackground(getResources().getDrawable(R.drawable.button_border_radius_background));
        button.setTextColor(getResources().getColor(R.color.pure_white));
        button.setTag("SELECT_ITEM");
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;

        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }
    }

}
