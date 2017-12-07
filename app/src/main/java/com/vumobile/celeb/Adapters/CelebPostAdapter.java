package com.vumobile.celeb.Adapters;

/**
 * Created by toukirul on 24/5/2017.
 */

//public class CelebPostAdapter {
//}

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.MyBounceInterpolator;
import com.vumobile.celeb.ui.EditPostActivity;
import com.vumobile.fan.login.ImageOrVideoView;
import com.vumobile.fan.login.ViaLive;
import com.vumobile.fan.login.model.FanNotificationModelEnity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by IT-10 on 5/3/2017.
 */

public class CelebPostAdapter extends RecyclerView.Adapter<CelebPostAdapter.MyViewHolder> {

    private List<FanNotificationModelEnity> fanNotificationModelEnities;
    Context mContext;
    String totalLike;
    ShareDialog shareDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public Button btn_edit;
        public ImageView editButton, deleteButton;
        public LinearLayout editDeleteLayout;
        public ImageView imageViewNotificationProfilePic, imageViewNotificationImage, imageViewNotificationLike, imageViewPlayIcon;
        public TextView textViewNotificationCelebName, textViewNotificationTime, textViewNotificationMessage, textViewNotificationLikeCount;
        public LinearLayout linearLayoutMain;
        public ImageView videoViewNotif;
        public RelativeLayout relativeLayoutImageAndVideo;
        private ImageView imgShareFb;

        public MyViewHolder(View view) {
            super(view);
            FacebookSdk.sdkInitialize(mContext);
            imgShareFb = (ImageView) view.findViewById(R.id.imgShareFb);
            editButton = (ImageView) view.findViewById(R.id.editButton);
            deleteButton = (ImageView) view.findViewById(R.id.deleteButton);
            btn_edit = (Button) view.findViewById(R.id.btn_edit);
            editDeleteLayout = (LinearLayout) view.findViewById(R.id.editDeleteLayout);
            linearLayoutMain = (LinearLayout) view.findViewById(R.id.linearLayoutMain);
            imageViewNotificationProfilePic = (ImageView) view.findViewById(R.id.imageViewNotificationProfilePic);
            imageViewNotificationImage = (ImageView) view.findViewById(R.id.imageViewNotificationImage);
            imageViewNotificationLike = (ImageView) view.findViewById(R.id.imageViewNotificationLike);
            imageViewPlayIcon = (ImageView) view.findViewById(R.id.imageViewPlayIcon);

            textViewNotificationCelebName = (TextView) view.findViewById(R.id.textViewNotificationCelebName);
            textViewNotificationTime = (TextView) view.findViewById(R.id.textViewNotificationTime);
            textViewNotificationMessage = (TextView) view.findViewById(R.id.textViewNotificationMessage);
            textViewNotificationLikeCount = (TextView) view.findViewById(R.id.textViewNotificationLikeCount);

            videoViewNotif = (ImageView) view.findViewById(R.id.videoViewNotif);

            relativeLayoutImageAndVideo = (RelativeLayout) view.findViewById(R.id.relativeLayoutImageAndVideo);

        }
    }

    public CelebPostAdapter(Context context, List<FanNotificationModelEnity> fanNotificationModelEnities) {
        this.fanNotificationModelEnities = fanNotificationModelEnities;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_edit_post, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FanNotificationModelEnity fanNotificationModelEnity = fanNotificationModelEnities.get(position);

        Glide.with(mContext).load(fanNotificationModelEnity.getImage_url()).thumbnail(0.5f).into(holder.imageViewNotificationProfilePic);


        holder.textViewNotificationCelebName.setText(fanNotificationModelEnity.getName());
        holder.textViewNotificationTime.setText(fanNotificationModelEnity.getTimeStamp());
        holder.textViewNotificationMessage.setText(fanNotificationModelEnity.getPost());
        holder.textViewNotificationLikeCount.setText(fanNotificationModelEnity.getLikeCount());

        // tag
        holder.textViewNotificationCelebName.setTag(fanNotificationModelEnity.getId()); // set tag with msisdn to make a new like
        holder.textViewNotificationTime.setTag(fanNotificationModelEnity.getPost_Urls()); // set tag with image/video url
        holder.textViewNotificationMessage.setTag(fanNotificationModelEnity.getIsImage()); // 1 for image 2 for video
        holder.textViewNotificationLikeCount.setTag(fanNotificationModelEnity.getFlags_Notificaton()); // 1 is live , 2 is post

        holder.videoViewNotif.setVisibility(View.GONE);
        holder.imageViewNotificationImage.setVisibility(View.GONE);
        holder.imageViewPlayIcon.setVisibility(View.GONE);

        // share with facebook
        holder.imgShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = fanNotificationModelEnity.getPost_Urls();
                String postUrl = "";
                try {
                    postUrl = getUrlFromArray(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String commentText = "" + fanNotificationModelEnity.getPost();
                Log.d("share_content", "Post url " + postUrl + " Comment " + commentText);


                //        shareContentUrl(postUrl);


//               if (!postUrl.isEmpty() && commentText.isEmpty()){
//                   // share only url
//                   Log.d("share_content","share only url");
//
//
//               }


//               else if (!postUrl.isEmpty() && !commentText.isEmpty()){
//                   // share url and post
//                   Log.d("share_content","share url and post");
                shareContentNpost(postUrl, commentText);
//               }else if (postUrl.isEmpty() && !commentText.isEmpty()){
//                   // share only post
//                   Log.d("share_content","share only post");
//                   sharePost(commentText);
//               }

            }
        });

        // Edit or delete post
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", fanNotificationModelEnity.getId());
                intent.putExtra("isImage", fanNotificationModelEnity.getIsImage());
                intent.putExtra("post", fanNotificationModelEnity.getPost());
                intent.putExtra("post_urls", fanNotificationModelEnity.getUrl());
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Edit post", Toast.LENGTH_LONG).show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.editDeleteLayout.getVisibility() == View.VISIBLE) {
                    holder.editDeleteLayout.setVisibility(View.GONE);
                }

                deleteItem(fanNotificationModelEnity.getId());
                fanNotificationModelEnities.remove(position);
                notifyDataSetChanged();

                Toast.makeText(mContext, "Delete post", Toast.LENGTH_LONG).show();
            }
        });


        if (fanNotificationModelEnity.getFlags_Notificaton().equals("1")) {
            holder.imageViewNotificationImage.setVisibility(View.VISIBLE);
//            holder.imageViewNotificationImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.live_icon));
            Glide.with(mContext).load("").placeholder(mContext.getResources().getDrawable(R.drawable.live_icon)).into(holder.imageViewNotificationImage);
            holder.textViewNotificationMessage.setText("Live video...");
        }
        Log.d("ttt o", "onBindViewHolder: " + fanNotificationModelEnity.getFlags_Notificaton() + fanNotificationModelEnity.getName());


        JSONArray array = null;  //jsonObject.getJSONArray("result");
        try {
            array = new JSONArray(fanNotificationModelEnity.getPost_Urls());
            for (int a = 0; a < array.length(); a++) {
                if (!array.get(a).equals("")) {
                    Log.d("touhid", "image or video link: " + array.get(a));
                    if (fanNotificationModelEnity.getIsImage().equals("1")) {
                        holder.imageViewNotificationImage.setVisibility(View.VISIBLE);
                        holder.videoViewNotif.setTag(array.get(a));
                        Glide.with(mContext).load(array.get(a)).thumbnail(0.5f).into(holder.imageViewNotificationImage);
                    } else if (fanNotificationModelEnity.getIsImage().equals("2")) {
                        holder.videoViewNotif.setVisibility(View.VISIBLE);
                        holder.imageViewPlayIcon.setVisibility(View.VISIBLE);
//                        Uri uri = Uri.parse(array.get(a).toString()); //Declare your url here.
//                        holder.videoViewNotif.setVideoURI(uri);
//                        holder.videoViewNotif.setTag(uri);
//                        holder.videoViewNotif.seekTo(1000);
//                        holder.videoViewNotif.pause();
                        JSONArray array1 = new JSONArray(fanNotificationModelEnity.getPost_Urls());
                        holder.videoViewNotif.setTag(array1.get(0).toString());
                        Log.d("hhhhhhhhhh", array1.get(0).toString());
                        Picasso.with(mContext).load(fanNotificationModelEnity.getThumbImage()).into(holder.videoViewNotif);

                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        holder.linearLayoutMain.setOnClickListener(v -> {

            if (holder.editDeleteLayout.getVisibility() == View.VISIBLE) {
                holder.editDeleteLayout.setVisibility(View.GONE);
            } else {
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("IMG_OR_VID", holder.textViewNotificationMessage.getTag().toString());
                        intent.putExtra("IMG_OR_VID_URL", holder.videoViewNotif.getTag().toString());
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.editDeleteLayout.getVisibility() == View.GONE) {
                    holder.editDeleteLayout.setVisibility(View.VISIBLE);
                }else {
                    holder.editDeleteLayout.setVisibility(View.GONE);
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

    private String getUrlFromArray(String postUrl) throws JSONException {

        JSONArray jsonArray = new JSONArray(postUrl);
        String url = jsonArray.getString(0);

        return url;
    }

    // share text
    private void sharePost(String commentText) {
        Log.d("share_content", "Comment " + commentText);
        shareDialog = new ShareDialog((Activity) mContext);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("CelebApp")
                .setContentDescription(commentText)
                .setQuote(commentText)
                .build();
        shareDialog.show(linkContent);
    }

    // share url n text
    private void shareContentNpost(String postUrl, String commentText) {
        Log.d("share_content", "Post url " + postUrl + "Comment " + commentText);
        shareDialog = new ShareDialog((Activity) mContext);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("CelebApp")
                .setContentDescription(commentText)
                .setQuote(commentText)
                .setContentUrl(Uri.parse(postUrl)).build();
        shareDialog.show(linkContent, ShareDialog.Mode.NATIVE);
    }

    // share url
    private void shareContentUrl(String postUrl) {
        Log.d("share_content", "Post url " + postUrl);
        URI uri = null;
        try {
            uri = new URI(postUrl);
        } catch (URISyntaxException e) {

        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        mContext.startActivity(Intent.createChooser(shareIntent, "post image"));
    }

    private void deleteItem(String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_DELETE_POST + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String log = object.getString("result");
                            Log.d("sucessresult", log);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer", "" + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
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

