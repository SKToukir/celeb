package com.vumobile.fan.login.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.MyBounceInterpolator;
import com.vumobile.fan.login.ImageOrVideoView;
import com.vumobile.fan.login.ViaLive;
import com.vumobile.fan.login.model.FanNotificationModelEnity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by IT-10 on 5/3/2017.
 */

public class FanNotificationAdapter extends RecyclerView.Adapter<FanNotificationAdapter.MyViewHolder> {

    private List<FanNotificationModelEnity> fanNotificationModelEnities;
    Context mContext;
    String totalLike;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewNotificationProfilePic, imageViewNotificationImage, imageViewNotificationLike, imageViewPlayIcon;
        public TextView textViewNotificationCelebName, textViewNotificationTime, textViewNotificationMessage, textViewNotificationLikeCount;
        public LinearLayout linearLayoutMain;
        public ImageView imageViewThumb;
        public RelativeLayout relativeLayoutImageAndVideo;

        public MyViewHolder(View view) {
            super(view);
            linearLayoutMain = (LinearLayout) view.findViewById(R.id.linearLayoutMain);

            imageViewNotificationProfilePic = (ImageView) view.findViewById(R.id.imageViewNotificationProfilePic);
            imageViewNotificationImage = (ImageView) view.findViewById(R.id.imageViewNotificationImage);
            imageViewNotificationLike = (ImageView) view.findViewById(R.id.imageViewNotificationLike);
            imageViewPlayIcon = (ImageView) view.findViewById(R.id.imageViewPlayIcon);

            textViewNotificationCelebName = (TextView) view.findViewById(R.id.textViewNotificationCelebName);
            textViewNotificationTime = (TextView) view.findViewById(R.id.textViewNotificationTime);
            textViewNotificationMessage = (TextView) view.findViewById(R.id.textViewNotificationMessage);
            textViewNotificationLikeCount = (TextView) view.findViewById(R.id.textViewNotificationLikeCount);

            imageViewThumb = (ImageView) view.findViewById(R.id.imageViewThumb);

            relativeLayoutImageAndVideo = (RelativeLayout) view.findViewById(R.id.relativeLayoutImageAndVideo);

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

        Glide.with(mContext)
                .load(fanNotificationModelEnity.getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewNotificationProfilePic);


        holder.textViewNotificationCelebName.setText(fanNotificationModelEnity.getName());
        holder.textViewNotificationTime.setText(fanNotificationModelEnity.getTimeStamp());
        holder.textViewNotificationMessage.setText(fanNotificationModelEnity.getPost());
        holder.textViewNotificationLikeCount.setText(fanNotificationModelEnity.getLikeCount());

        // tag
        holder.textViewNotificationCelebName.setTag(fanNotificationModelEnity.getId()); // set tag with msisdn to make a new like
        holder.textViewNotificationTime.setTag(fanNotificationModelEnity.getPost_Urls()); // set tag with image/video url
        holder.textViewNotificationMessage.setTag(fanNotificationModelEnity.getIsImage()); // 1 for image 2 for video
        holder.textViewNotificationLikeCount.setTag(fanNotificationModelEnity.getFlags_Notificaton()); // 1 is live , 2 is post

        holder.imageViewThumb.setVisibility(View.GONE);
        holder.imageViewNotificationImage.setVisibility(View.GONE);
        holder.imageViewPlayIcon.setVisibility(View.GONE);


        if (fanNotificationModelEnity.getFlags_Notificaton().equals("1")) {
//            holder.imageViewNotificationImage.setVisibility(View.VISIBLE);
////            holder.imageViewNotificationImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.live_icon));
//            Glide.with(mContext).load("")
//                    .placeholder(mContext.getResources().getDrawable(R.drawable.live_icon))
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.imageViewNotificationImage);
//            holder.textViewNotificationMessage.setText("Live video...");
        } else {

        }
        Log.d("ttt o", "onBindViewHolder: " + fanNotificationModelEnity.getFlags_Notificaton() + fanNotificationModelEnity.getName());


        JSONArray array = null;  //jsonObject.getJSONArray("result");
        JSONArray array2 = null;  //jsonObject.getJSONArray("result");
        try {
            array = new JSONArray(fanNotificationModelEnity.getPost_Urls());
            array2 = new JSONArray(fanNotificationModelEnity.getNotifVideoThumb());
            Log.d("touhid 0", "onBindViewHolder: " + array2.toString());
            Log.d("touhid 1", "onBindViewHolder: " + fanNotificationModelEnity.toString());
            for (int a = 0; a < array.length(); a++) {
                if (!array.get(a).equals("")) {
                    Log.d("touhid 2", "image or video link: " + array.get(a));
                    if (fanNotificationModelEnity.getIsImage().equals("1")) { // image
                        holder.imageViewNotificationImage.setVisibility(View.VISIBLE);
                        Log.d("touhid 3", "onBindViewHolder: " + array.get(a));
                        holder.imageViewThumb.setTag(array.get(a));
                        Picasso.with(mContext)
                                .load(array.get(a).toString())
                                .into(holder.imageViewNotificationImage);
                    } else if (fanNotificationModelEnity.getIsImage().equals("2")) { // video
                        holder.imageViewThumb.setVisibility(View.VISIBLE);
                        holder.imageViewPlayIcon.setVisibility(View.VISIBLE);
//                        Uri uri = Uri.parse(array.get(a).toString()); //Declare your url here.
//                        holder.imageViewThumb.setImageURI(uri);
                        Log.d("touhid u", "onBindViewHolder: " + array2.get(a).toString());
                        Picasso.with(mContext).load(array2.get(a).toString())
                                .into(holder.imageViewThumb);
                        holder.imageViewThumb.setTag(array.get(a));
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        holder.linearLayoutMain.setOnClickListener(v -> {

            if (fanNotificationModelEnity.getFlags_Notificaton().equals("1")) {
                // Go to live
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.CELEB_IS_ONLINE + fanNotificationModelEnity.getMSISDN(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("FromServer 19", jsonObject.toString());
                        try {

                            if (!jsonObject.getString("result").equals("0")) {
                                Intent intent = new Intent(mContext, ViaLive.class);
                                intent.putExtra("CELEB_FB_NAME", holder.textViewNotificationCelebName.getText().toString());
                                mContext.startActivity(intent);
                            } else {
                                Toast.makeText(mContext, holder.textViewNotificationCelebName.getText().toString() + " is offline now.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);

                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                //Adding request to the queue
                requestQueue.add(request);


            } else {
                try {

                    Intent intent = new Intent(mContext, ImageOrVideoView.class);
                    intent.putExtra("IMG_OR_VID", holder.textViewNotificationMessage.getTag().toString());
                    Log.d("touhid link", "onBindViewHolder: " + holder.imageViewThumb.getTag().toString());
                    intent.putExtra("IMG_OR_VID_URL", holder.imageViewThumb.getTag().toString());
                    mContext.startActivity(intent);


                }catch (Exception e){

                }

            }

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


//        //  Get convert form and set date
//        String dtStart = fanNotificationModelEnity.getTimeStamp(); //"2010-10-15T18:13:42.607"; // "2010-10-15T09:27:370";
//        try {
//            DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            DateFormat formatter = new SimpleDateFormat("dd-mm-yy hh:mm a");
//            Date convertedDate = parser.parse(dtStart);
//            String output = formatter.format(convertedDate);
//            Log.d("tttd", "onBindViewHolder: " + output);
//            // make am pm to capital and remove .
//            output = output.replace(".", "").replace("a", "A").replace("p", "P").replace("m", "M");
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

