package com.vumobile.fan.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.MyBounceInterpolator;
import com.vumobile.fan.login.model.FanNotificationModelEnity;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IT-10 on 5/3/2017.
 */

public class FanNotificationAdapter extends RecyclerView.Adapter<FanNotificationAdapter.MyViewHolder> {

    private List<FanNotificationModelEnity> fanNotificationModelEnities;
    Context mContext;
    String totalLike;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewNotificationProfilePic, imageViewNotificationImage, imageViewNotificationLike;
        public TextView textViewNotificationCelebName, textViewNotificationTime, textViewNotificationMessage, textViewNotificationLikeCount;
        public LinearLayout linearLayoutMain;

        public MyViewHolder(View view) {
            super(view);
            linearLayoutMain = (LinearLayout) view.findViewById(R.id.linearLayoutMain);

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

        Glide.with(mContext).load(fanNotificationModelEnity.getProfileImageUrl()).thumbnail(0.5f).into(holder.imageViewNotificationProfilePic);
        Glide.with(mContext).load(fanNotificationModelEnity.getNotificationImageUrl()).thumbnail(0.5f).into(holder.imageViewNotificationImage);

        holder.textViewNotificationCelebName.setText(fanNotificationModelEnity.getName());


        //  Get convert form and set date
        String dtStart = fanNotificationModelEnity.getTime(); //"2010-10-15T18:13:42.607"; // "2010-10-15T09:27:370";
        try {
            DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat formatter = new SimpleDateFormat("dd-mm-yy hh:mm a");
            Date convertedDate = parser.parse(dtStart);
            String output = formatter.format(convertedDate);
            Log.d("tttd", "onBindViewHolder: " + output);
            // make am pm to capital and remove .
            output = output.replace(".", "").replace("a", "A").replace("p", "P").replace("m", "M");

            holder.textViewNotificationTime.setText(output);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        holder.textViewNotificationMessage.setText(fanNotificationModelEnity.getMessage());
        holder.textViewNotificationLikeCount.setText(fanNotificationModelEnity.getLikeCount());
        holder.textViewNotificationCelebName.setTag(fanNotificationModelEnity.getId());

        holder.linearLayoutMain.setOnClickListener(v -> {
            Toast.makeText(mContext, "" + holder.textViewNotificationCelebName.getTag(), Toast.LENGTH_SHORT).show();
        });

        holder.imageViewNotificationLike.setOnClickListener(v -> {
            makeLikeAndFetchTotalLike(Api.URL_NOTIFICATION_LIKE_SET_GET, holder.textViewNotificationLikeCount, holder.textViewNotificationCelebName.getTag().toString());

            // button animation
            final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);

            holder.imageViewNotificationLike.startAnimation(myAnim);

        });


    }


    @Override
    public int getItemCount() {
        return fanNotificationModelEnities.size();
    }

    private String makeLikeAndFetchTotalLike(String notifLikeUrl, TextView likePlaceHolder, String tagNotifId) {

        String fullUrl = notifLikeUrl + "&ID=" + tagNotifId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, fullUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer notif like", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            totalLike = jsonObject.getString("result");
                            likePlaceHolder.setText(totalLike);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer notif like", "" + error.getMessage());
                        //    TastyToast.makeText(mContext, "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                });

        Volley.newRequestQueue(mContext).add(stringRequest);

        return totalLike;
    }


}
