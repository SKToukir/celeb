package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.GiftClass;

import java.util.List;

/**
 * Created by toukirul on 19/6/2017.
 */

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyViewHolder> {

    private Context mContext;
    private List<GiftClass> videoHomeList;


    public
    GiftsAdapter(Context context, List<GiftClass> videoHomeList) {
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

        List<String> giftList = primaryClass.getGiftList();

        Log.d("ListSize", String.valueOf(giftList.size()));

        for (int i = 0; i < giftList.size(); i++){

            Log.d("ListSize", giftList.get(i));
        }

//        Glide.with(mContext).load(primaryClass.getGiftOne()).override(100, 100).thumbnail(0.1f).into(holder.imgGiftOne);
//        Glide.with(mContext).load(primaryClass.getGiftTwo()).override(100, 100).thumbnail(0.1f).into(holder.imgGiftTwo);
//        Glide.with(mContext).load(primaryClass.getGiftThree()).override(100, 100).thumbnail(0.1f).into(holder.imgGiftThree);

    }

    @Override
    public int getItemCount() {
        return videoHomeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImageView, imgGiftOne, imgGiftTwo, imgGiftThree;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgGiftOne = (ImageView) itemView.findViewById(R.id.imgGiftOne);
            imgGiftTwo = (ImageView) itemView.findViewById(R.id.imgGiftTwo);
            imgGiftThree = (ImageView) itemView.findViewById(R.id.imgGiftThree);
            videoImageView = (ImageView) itemView.findViewById(R.id.imgGiftSender);

        }
    }
}
