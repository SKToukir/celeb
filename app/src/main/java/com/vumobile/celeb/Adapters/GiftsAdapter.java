package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.Gift;
import com.vumobile.celeb.model.GiftClass;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toukirul on 19/6/2017.
 */

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyViewHolder> {


    private List<Gift> giftList = new ArrayList<>();
    private Gift gift;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private List<GiftClass> videoHomeList;


    public GiftsAdapter(Context context, List<GiftClass> videoHomeList) {
        this.mContext = context;
        this.videoHomeList = videoHomeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gift, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        GiftClass primaryClass = videoHomeList.get(position);

        Glide.with(mContext).load(primaryClass.getImageUrl()).override(100, 100).thumbnail(0.1f).into(holder.videoImageView);

        Log.d("FromServer",primaryClass.getListOfGift().toString());

        JSONArray array = primaryClass.getListOfGift();

        for (int i = 0; i < array.length(); i++){

            gift = new Gift();
            try {
                gift.setGiftUrl(array.getString(i));

                giftList.add(gift);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    public int getItemCount() {
        return videoHomeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            adapter = new SingleGiftAdapter(mContext,giftList);
            videoImageView = (ImageView) itemView.findViewById(R.id.imgGiftSender);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_gifts);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL));

        }
    }
}
