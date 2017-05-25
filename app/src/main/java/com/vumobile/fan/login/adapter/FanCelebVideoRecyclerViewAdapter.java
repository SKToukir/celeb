package com.vumobile.fan.login.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.vumobile.celeb.R;
import com.vumobile.fan.login.model.FanCelebVideoModelEntity;

import java.util.List;

/**
 * Created by IT-10 on 5/7/2017.
 */

public class FanCelebVideoRecyclerViewAdapter extends RecyclerView.Adapter<FanCelebVideoRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private List<FanCelebVideoModelEntity> fanCelebVideoModelEntities;


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {// implements View.OnClickListener
        public VideoView imageViewRecyclerItemVThumb;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewRecyclerItemVThumb = (VideoView) itemView.findViewById(R.id.imageViewRecyclerItemVThumb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Data is passed into the constructor
    public FanCelebVideoRecyclerViewAdapter(Context context, List<FanCelebVideoModelEntity> fanCelebVideoModelEntities) {
        this.context = context;
        this.fanCelebVideoModelEntities = fanCelebVideoModelEntities;
    }

    // Inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fan_celeb_video_recycler_item, parent, false);



        return new ViewHolder(itemView);
    }

    // binds the data to the textView in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FanCelebVideoModelEntity fanCelebVideoModelEntity = fanCelebVideoModelEntities.get(position);

        Uri uri = Uri.parse(fanCelebVideoModelEntity.getVideoUrl()); //Declare your url here.
//        MediaController mediaController = new MediaController(context);
//        mediaController.setAnchorView(holder.videoViewMainPlayer);
//        holder.videoViewMainPlayer.setMediaController(mediaController);
        holder.imageViewRecyclerItemVThumb.setVideoURI(uri);
        holder.imageViewRecyclerItemVThumb.seekTo(3000);
        holder.imageViewRecyclerItemVThumb.pause();
      //  holder.videoViewMainPlayer.requestFocus();
       // holder.videoViewMainPlayer.start();

        holder.imageViewRecyclerItemVThumb.setTag(fanCelebVideoModelEntity.getVideoUrl());

        // holder.imageViewRecyclerItem.setImageDrawable(context.getResources().getDrawable(R.drawable.unfollow));
        Log.d("adapter ttt", "onBindViewHolder: " + fanCelebVideoModelEntity.getVideoUrl());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return fanCelebVideoModelEntities.size();
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
