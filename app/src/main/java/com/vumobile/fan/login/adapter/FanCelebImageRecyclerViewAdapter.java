package com.vumobile.fan.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.model.FanCelebImageModelEntity;

import java.util.List;

/**
 * Created by IT-10 on 5/7/2017.
 */

public class FanCelebImageRecyclerViewAdapter extends RecyclerView.Adapter<FanCelebImageRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private List<FanCelebImageModelEntity> fanCelebImageModelEntities;


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {// implements View.OnClickListener
        public ImageView imageViewRecyclerItem;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewRecyclerItem = (ImageView) itemView.findViewById(R.id.imageViewRecyclerItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Data is passed into the constructor
    public FanCelebImageRecyclerViewAdapter(Context context, List<FanCelebImageModelEntity> fanCelebImageModelEntities) {
        //  this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fanCelebImageModelEntities = fanCelebImageModelEntities;
    }

    // Inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fan_celeb_image_recycler_item, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FanCelebImageModelEntity fanCelebImageModelEntity = fanCelebImageModelEntities.get(position);

        String isImage = fanCelebImageModelEntity.getIsImage();

        Glide.with(context)
                .load(fanCelebImageModelEntity.getImageUrl())
                .thumbnail(.1f)
                .into(holder.imageViewRecyclerItem);



        // holder.imageViewRecyclerItem.setImageDrawable(context.getResources().getDrawable(R.drawable.unfollow));
        Log.d("adapter ttt", "onBindViewHolder: " + fanCelebImageModelEntity.getImageUrl());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return fanCelebImageModelEntities.size();
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
