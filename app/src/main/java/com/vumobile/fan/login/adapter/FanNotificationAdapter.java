package com.vumobile.fan.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.model.FanNotificationModelEnity;

import java.util.List;

/**
 * Created by IT-10 on 5/3/2017.
 */

public class FanNotificationAdapter extends RecyclerView.Adapter<FanNotificationAdapter.MyViewHolder> {

    private List<FanNotificationModelEnity> fanNotificationModelEnities;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewNotificationProfilePic, imageViewNotificationImage, imageViewNotificationLike;
        public TextView textViewNotificationCelebName, textViewNotificationTime, textViewNotificationMessage, textViewNotificationLikeCount;

        public MyViewHolder(View view) {
            super(view);
            imageViewNotificationProfilePic = (ImageView) view.findViewById(R.id.imageViewNotificationProfilePic);
            imageViewNotificationImage = (ImageView) view.findViewById(R.id.imageViewNotificationImage);
            imageViewNotificationLike = (ImageView) view.findViewById(R.id.imageViewNotificationLike);

            textViewNotificationCelebName = (TextView) view.findViewById(R.id.textViewNotificationCelebName);
            textViewNotificationTime = (TextView) view.findViewById(R.id.textViewNotificationTime);
            textViewNotificationMessage = (TextView) view.findViewById(R.id.textViewNotificationMessage);
            textViewNotificationLikeCount = (TextView) view.findViewById(R.id.textViewNotificationLikeCount);
        }
    }

    public FanNotificationAdapter(Context context, List<FanNotificationModelEnity> fanNotificationModelEnities) {
        this.fanNotificationModelEnities = fanNotificationModelEnities;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_fan_notification_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FanNotificationModelEnity fanNotificationModelEnity = fanNotificationModelEnities.get(position);

        Glide.with(mContext).load(fanNotificationModelEnity.getProfileImageUrl()).into(holder.imageViewNotificationProfilePic);
        Glide.with(mContext).load(fanNotificationModelEnity.getNotificationImageUrl()).into(holder.imageViewNotificationImage);

        holder.textViewNotificationCelebName.setText(fanNotificationModelEnity.getName());
        holder.textViewNotificationTime.setText(fanNotificationModelEnity.getTime());
        holder.textViewNotificationMessage.setText(fanNotificationModelEnity.getMessage());
        holder.textViewNotificationLikeCount.setText(fanNotificationModelEnity.getLikeCount());
        //  holder.genre.setText(movie.getGenre());
    }

    @Override
    public int getItemCount() {
        return fanNotificationModelEnities.size();
    }
}
