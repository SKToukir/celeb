package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.GiftsUser;

import java.util.List;

/**
 * Created by toukirul on 4/7/2017.
 */

public class SingleUserGiftsAdapter extends RecyclerView.Adapter<SingleUserGiftsAdapter.MyViewHolder> {

    private Context mContext;
    private List<GiftsUser> giftsUsers;


    public SingleUserGiftsAdapter(Context context, List<GiftsUser> videoHomeList) {
        this.mContext = context;
        this.giftsUsers = videoHomeList;
    }

    @Override
    public SingleUserGiftsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_gift, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GiftsUser primaryClass = giftsUsers.get(position);

        Glide.with(mContext).load(primaryClass.getImageUrl()).override(100, 100).thumbnail(0.1f).into(holder.imgSingleGift);
    }

    @Override
    public int getItemCount() {
        return giftsUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSingleGift;


        public MyViewHolder(View itemView) {
            super(itemView);
            imgSingleGift = (ImageView) itemView.findViewById(R.id.imgSingleGift);

        }
    }
}