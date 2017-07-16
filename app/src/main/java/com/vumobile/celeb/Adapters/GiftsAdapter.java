package com.vumobile.celeb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.GiftClass;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by toukirul on 19/6/2017.
 */

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyViewHolder> {

    private Context mContext;
    private List<GiftClass> videoHomeList;
    View view;

    public GiftsAdapter(Context context, List<GiftClass> videoHomeList) {
        this.mContext = context;
        this.videoHomeList = videoHomeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gift, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        GiftClass primaryClass = videoHomeList.get(position);

        Glide.with(mContext).load(primaryClass.getImageUrl()).override(100, 100).into(holder.videoImageView); //.thumbnail(0.5f)

        JSONArray jsArURL = primaryClass.getArray();

        for (int i = 0; i < jsArURL.length(); i++) {
            try {
                String mUrl = jsArURL.getString(i);
                if (!mUrl.equals("")) {
                    ImageView imageView = new ImageView(mContext);
                    Glide.with(mContext).load(mUrl).override(80, 80).into(imageView);
                    holder.linearLayoutGiftThumbnailList.addView(imageView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        holder.txtTotalGifts.setText("" + primaryClass.getTotalGifts());

    }

    @Override
    public int getItemCount() {
        return videoHomeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView videoImageView;
        TextView txtTotalGifts;
        LinearLayout linearLayoutGiftThumbnailList;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTotalGifts = (TextView) itemView.findViewById(R.id.txtTotalGifts);
            videoImageView = (CircleImageView) itemView.findViewById(R.id.imgGiftSender);
            linearLayoutGiftThumbnailList = (LinearLayout) itemView.findViewById(R.id.linearLayoutGiftThumbnailList);
        }

    }
}
