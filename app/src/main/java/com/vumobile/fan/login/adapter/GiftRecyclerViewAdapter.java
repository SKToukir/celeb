package com.vumobile.fan.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.model.GiftItemModel;

import java.util.List;

/**
 * Created by IT-10 on 5/7/2017.
 */

public class GiftRecyclerViewAdapter extends RecyclerView.Adapter<GiftRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private List<GiftItemModel> giftItemModels;


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {// implements View.OnClickListener
        public ImageView imageViewGiftItem;
        public TextView textViewPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewGiftItem = (ImageView) itemView.findViewById(R.id.imageViewGiftItem);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Data is passed into the constructor
    public GiftRecyclerViewAdapter(Context context, List<GiftItemModel> giftItemModels) {
        //  this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.giftItemModels = giftItemModels;
    }

    // Inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gift, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GiftItemModel giftItemModel = giftItemModels.get(position);

        String contentTitle = giftItemModel.getContentTitle();
        String contentUrl = giftItemModel.getPreviewURL();

        holder.textViewPrice.setTag(giftItemModel.getContentTitle());

        Glide.with(context)
                .load("http://wap.shabox.mobi/CMS/GraphicsPreview/Stickers/" + contentUrl)
                .into(holder.imageViewGiftItem);

        Log.d("adapter ttt", "onBindViewHolder: " + giftItemModel.getPreviewURL());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return giftItemModels.size();
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
