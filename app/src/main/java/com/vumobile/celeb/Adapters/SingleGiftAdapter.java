package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.Gift;

import java.util.List;

/**
 * Created by toukirul on 20/6/2017.
 */

public class SingleGiftAdapter extends RecyclerView.Adapter<SingleGiftAdapter.MyViewHolder> {


    private Gift gift;
    private Context mContext;
    private List<Gift> videoHomeList;


    public SingleGiftAdapter(Context context, List<Gift> videoHomeList) {
        this.mContext = context;
        this.videoHomeList = videoHomeList;
    }

    @Override
    public SingleGiftAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_gift, parent, false);
        return new SingleGiftAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Gift primaryClass = videoHomeList.get(position);

        Glide.with(mContext).load(primaryClass.getGiftUrl()).override(100, 100).thumbnail(0.1f).into(holder.videoImageView);

    }


    @Override
    public int getItemCount() {
        return videoHomeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            videoImageView = (ImageView) itemView.findViewById(R.id.imgSingleGift);

        }
    }
}