package com.vumobile.fan.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.ui.BaseActivity;
import com.vumobile.celeb.ui.ChatRoomActivity;
import com.vumobile.celeb.ui.LiveRoomActivity;
import com.vumobile.celeb.ui.MessageActivity;
import com.vumobile.fan.login.ui.FanCelebProfileImageVideo;
import com.vumobile.fan.login.ui.FanNotificationActivity;
import com.vumobile.fan.login.ui.fragment.Gifts;
import com.vumobile.utils.MyInternetCheckReceiver;
import com.vumobile.videocall.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FanCelebProfileActivity extends BaseActivity implements View.OnClickListener {


    String msisdn, name, fbName, profilePic, followerCount;

    ImageView imageViewNotification, imageViewMessage, imageViewHome;
    ImageView imageViewVideoCall, imageViewChat, imageViewImageAndVideo, imageViewGift;
    LinearLayout activity_fan_celeb_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_celeb_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        imageViewNotification = (ImageView) toolbar.findViewById(R.id.imageViewNotification);
        imageViewMessage = (ImageView) toolbar.findViewById(R.id.imageViewMessage);
        imageViewHome = (ImageView) toolbar.findViewById(R.id.imageViewHome);
        imageViewNotification.setOnClickListener(this);
        imageViewMessage.setOnClickListener(this);
        imageViewHome.setOnClickListener(this);

        imageViewVideoCall = (ImageView) findViewById(R.id.imageViewVideoCall);
        imageViewChat = (ImageView) findViewById(R.id.imageViewChat);
        imageViewImageAndVideo = (ImageView) findViewById(R.id.imageViewImageAndVideo);
        imageViewGift = (ImageView) findViewById(R.id.imageViewGift);
        imageViewVideoCall.setOnClickListener(this);
        imageViewChat.setOnClickListener(this);
        imageViewImageAndVideo.setOnClickListener(this);
        imageViewGift.setOnClickListener(this);

        activity_fan_celeb_profile = (LinearLayout) findViewById(R.id.activity_fan_celeb_profile);


        CircleImageView imageViewProfilePicFan = (CircleImageView) findViewById(R.id.imageViewProfilePicFan);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        TextView textViewFollowerCountProfile = (TextView) findViewById(R.id.textViewFollowerCountProfile);

        Intent intent = getIntent();
        msisdn = intent.getStringExtra("msisdn");
        name = intent.getStringExtra("name");
        fbName = intent.getStringExtra("fbname");
        profilePic = intent.getStringExtra("profilePic");
        followerCount = intent.getStringExtra("FOLLOWER");

        Picasso.with(getApplicationContext()).load(profilePic).into(imageViewProfilePicFan);
        textViewName.setText(fbName);
        textViewFollowerCountProfile.setText(followerCount);

        // show snackbar while no internet
        MyInternetCheckReceiver.isNetworkAvailableShowSnackbar(this, activity_fan_celeb_profile);
        new MyInternetCheckReceiver(activity_fan_celeb_profile);
    } // end of onCreate


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageViewNotification:
                startActivity(new Intent(getApplicationContext(), FanNotificationActivity.class));
                break;

            case R.id.imageViewMessage:
                Intent i = new Intent(this, MessageActivity.class);
                startActivity(i);
                break;

            case R.id.imageViewHome:
                finish();
                break;
            case R.id.imageViewVideoCall:

                String fan_msisdns = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
                // show chat request dialog
                //chatRequestDialog(msisdn, fan_msisdn, name, "1");
                requestForVideoCall(msisdn, fan_msisdns, "2");

                break;

            case R.id.imageViewChat:
                String fan_msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
                // show chat request dialog
                //chatRequestDialog(msisdn, fan_msisdn, name, "1");
                requestForChat(msisdn, fan_msisdn, "1");
                break;

            case R.id.imageViewImageAndVideo:
                Intent intent = new Intent(getApplicationContext(), FanCelebProfileImageVideo.class);
                intent.putExtra("MSISDN", msisdn);
                startActivity(intent);
                break;

            case R.id.imageViewGift:
                // get fragment manager
                FragmentManager fm = getSupportFragmentManager();
                // add
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
                ft.add(R.id.frameLayoutGifts, new Gifts());
                ft.addToBackStack(null);
                ft.commit();
                break;
        }

    }

    private void messageDialog(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void requestForChat(String celeb_msisdn, String fan_msisdn, String type) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_CHAT_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            String request_status = obj.getString("result").replaceAll(" ", "_");
                            Log.d("FromServer", request_status);

                            if (request_status.matches("Request_Pending") || request_status.equals("Request_Pending")) {
                                TastyToast.makeText(getApplicationContext(), "Your request is pending", TastyToast.LENGTH_LONG, TastyToast.INFO);

                            } else if (request_status.matches("Accepted")) {

                              checkScheduleTime(Api.URL_CHECK_SCHEDULE_TIME,fan_msisdn,msisdn,"1");
                                //TastyToast.makeText(getApplicationContext(),"Start Chat Activity!",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);

//                                startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));

//                                intent.putExtra("imageUrl",profilePic);
//                                intent.putExtra("name",fbName);
//                                startActivity(intent);
                              //  TastyToast.makeText(getApplicationContext(), "Start Chat Activity!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                //startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));


                            } else {
                                TastyToast.makeText(getApplicationContext(), "Your request is successfully sent to celeb!\nWait for confirmation", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            }

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
                }) {
            @Override
            protected Map<String, String> getParams() {

                /*
                *  request flag = 1 means it is a chat request
                *  request flag = 2 means it is a video request
                * */

                Map<String, String> params = new HashMap<>();
                params.put("Fan", fan_msisdn);
                params.put("Celebrity", celeb_msisdn);
                params.put("RequestType", type);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void requestForVideoCall(String celeb_msisdn, String fan_msisdn, String type) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_CHAT_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());


                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            String request_status = obj.getString("result").replaceAll(" ", "_");
                            Log.d("FromServer", request_status);


                            if (request_status.matches("Request_Pending") || request_status.equals("Request_Pending")) {
                                TastyToast.makeText(getApplicationContext(), "Your request is pending", TastyToast.LENGTH_LONG, TastyToast.INFO);

                            } else if (request_status.matches("Accepted")) {

                                checkScheduleTime(Api.URL_CHECK_SCHEDULE_TIME,fan_msisdn,msisdn,"2");



                            } else {
                                TastyToast.makeText(getApplicationContext(), "Your request is successfully sent to celeb!\nWait for confirmation", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            }


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
                }) {
            @Override
            protected Map<String, String> getParams() {

                /*
                *  request flag = 1 means it is a chat request
                *  request flag = 2 means it is a video request
                * */

                Map<String, String> params = new HashMap<String, String>();
                params.put("Fan", fan_msisdn);
                params.put("Celebrity", celeb_msisdn);
                params.put("RequestType", type);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_celeb) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    public void forwardToLiveRoom(int cRole) {

        // here put the room name
        //roomname@ which room user wants to join
        //String room = msisdn;
        String room = fbName;
        Intent i = new Intent(FanCelebProfileActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
        i.putExtra("user", "fan");
        startActivity(i);
    }

    String duration = null;

    private void setCalldurationDialog() {

        SeekBar seekBar;
        TextView textView;
        Button dialogButtonOK;


        final Dialog dialog = new Dialog(FanCelebProfileActivity.this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Set call duaration");
        seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
        textView = (TextView) dialog.findViewById(R.id.textView);
        dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                textView.setText("Call Duration: " + seekBarProgress +" min");

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Call Duration: " + seekBarProgress +  " min");
                duration = String.valueOf(seekBarProgress);
                //Toast.makeText(getApplicationContext(), "SeekBar Touch Stop ", Toast.LENGTH_SHORT).show();
            }
        });

        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("duartion", "Call duartion: " + duration);
                try {
                    //startActivity(new Intent(CelebHomeActivity.this,FanCelebProfileImageVideo.class));
                    if (!duration.equals("null") || duration != null && duration != "0" && duration.matches("0")) {
                        dialog.dismiss();
                        String fan_name = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
                        // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("celeb_name", fbName);
                        intent.putExtra("fan_name", fan_name);
                        intent.putExtra("profilePic", profilePic);
                        intent.putExtra("celeb_msisdn", msisdn);
                        startActivity(intent);

                    } else {
                        TastyToast.makeText(FanCelebProfileActivity.this, "Set your call duration first", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                        dialog.dismiss();
                    }
                } catch (NullPointerException e) {
                    TastyToast.makeText(FanCelebProfileActivity.this, "Set your call duration first", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    public void checkScheduleTime(String url, String fanMsisdn, String celebMsisdn, String type){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String scheduleTime = obj.getString("result");

                            if (!scheduleTime.replaceAll(" ","_").equals("Cannot_find_any_schedule")) {

                                if (type.equals("1")){
                                    boolean isTime = isTime(scheduleTime);
                                    if (isTime){
                                        String fan_msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
                                        String room_name = celebMsisdn + fan_msisdn;
                                        Log.d("room_name", room_name);
                                        Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class); //ChatViewActivity
                                        intent.putExtra("CELEB_PIC", profilePic);
                                        intent.putExtra("CELEB_NAME", fbName);
                                        intent.putExtra("room", room_name);
                                        startActivity(intent);
                                    }else {
                                        messageDialog("Your Schedule time is "+scheduleTime +" and you can not chat before your schedule time.");
                                    }
                                }else if (type.equals("2")){
                                    boolean isTime = isTime(scheduleTime);
                                    if (isTime){
                                        setCalldurationDialog();
                                    }else {
                                        messageDialog("Your Schedule time is "+scheduleTime +" and you can not call before your schedule time.");
                                    }
                                }

                            }else {
                                Log.d("FromServer", scheduleTime);
                            }

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
                }) {
            @Override
            protected Map<String, String> getParams() {

                /*
                *  request flag = 1 means it is a chat request
                *  request flag = 2 means it is a video request
                * */

                Map<String, String> params = new HashMap<>();
                params.put("Fan", fanMsisdn);
                params.put("Celebrity", celebMsisdn);
                params.put("RequestType", type);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean isTime(String scheduleTime) {

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:s a");
        dateFormat.format(date);
        System.out.println(dateFormat.format(date));

        try {
            if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(scheduleTime)))
            {
                System.out.println("Current time is greater than "+scheduleTime);
                Log.d("FromServer","True");
                return true;
            }else{
                System.out.println("Current time is less than "+scheduleTime);
                Log.d("FromServer","False");
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
}
