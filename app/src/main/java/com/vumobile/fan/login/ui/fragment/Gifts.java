package com.vumobile.fan.login.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.adapter.GiftRecyclerViewAdapter;
import com.vumobile.fan.login.model.GiftItemModel;
import com.vumobile.fan.login.serverrequest.AllVolleyInterfaces;
import com.vumobile.fan.login.serverrequest.MyVolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class Gifts extends Fragment {

    private static final String TAG = "Gift Fragment";
    private GiftRecyclerViewAdapter giftRecyclerViewAdapter;
    private RecyclerView recyclerViewGift;
    private SwipeRefreshLayout swipeRefreshLayoutGift;

    GiftItemModel giftItemModel;
    List<GiftItemModel> giftItemModels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gifts, container, false);
        //    linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);


        // swipe to refresh
        swipeRefreshLayoutGift = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutGift);
        swipeRefreshLayoutGift.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchGiftImages();
            }
        });

        swipeRefreshLayoutGift.post(() -> {
            fetchGiftImages();
        });


        // Data models
        giftItemModels = new ArrayList<>();

        recyclerViewGift = (RecyclerView) rootView.findViewById(R.id.recyclerViewGift);
        recyclerViewGift.setItemAnimator(new DefaultItemAnimator());


        // Set up the RecyclerView for image
        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerViewGift.setLayoutManager(gridLayoutManager);

        giftRecyclerViewAdapter = new GiftRecyclerViewAdapter(getActivity(), giftItemModels);

        giftRecyclerViewAdapter.setClickListener((view, position) -> {
            Toast.makeText(getActivity().getApplicationContext(), "Gift item :" + view.findViewById(R.id.textViewPrice).getTag(), Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    private void fetchGiftImages() {

        swipeRefreshLayoutGift.setRefreshing(true);
        giftItemModels.clear();

        String url = "http://wap.shabox.mobi/sticker_app_server/default.aspx?&contentCode=NEW_CONTENT";
        MyVolleyRequest.getAllGifts(getActivity().getApplicationContext(), Request.Method.GET, url, new AllVolleyInterfaces.ResponseString() {
            @Override
            public void getResponse(String responseResult) {
                Log.d("gift", "getResponse: " + responseResult);
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("stickers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                        giftItemModel = new GiftItemModel(
                                jo.getString("GraphicsCode"),
                                jo.getString("ContentTitle"),
                                jo.getString("PreviewURL")
                        );
                        Log.d(TAG, "getResponse: " + giftItemModel.toString());

                        giftItemModels.add(giftItemModel);
                        recyclerViewGift.setAdapter(giftRecyclerViewAdapter);
                        giftRecyclerViewAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {

                }

            }

            @Override
            public void getResponseErr(String responseResultErr) {
                Log.d("gift", "getResponseErr: " + responseResultErr);
            }
        });

        swipeRefreshLayoutGift.setRefreshing(false);
    }


}
