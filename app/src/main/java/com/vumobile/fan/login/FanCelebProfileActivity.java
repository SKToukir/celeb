package com.vumobile.fan.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FanCelebProfileActivity extends BaseActivity implements View.OnClickListener {

    String msisdn, name, fbName, profilePic;

    ImageView imageViewNotification, imageViewMessage, imageViewHome;
    ImageView imageViewVideoCall, imageViewChat, imageViewImageAndVideo, imageViewGift;


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

        CircleImageView imageViewProfilePicFan = (CircleImageView) findViewById(R.id.imageViewProfilePicFan);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);

        Intent intent = getIntent();
        msisdn = intent.getStringExtra("msisdn");
        name = intent.getStringExtra("name");
        fbName = intent.getStringExtra("fbname");
        profilePic = intent.getStringExtra("profilePic");

        Picasso.with(getApplicationContext()).load(profilePic).into(imageViewProfilePicFan);
        textViewName.setText(fbName);


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
                Toast.makeText(this, "v call", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageViewChat:
                String fan_msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
                // show chat request dialog
                chatRequestDialog(msisdn, fan_msisdn, name, "1");
                break;

            case R.id.imageViewImageAndVideo:
                Intent intent = new Intent(getApplicationContext(), FanCelebProfileImageVideo.class);
                intent.putExtra("MSISDN", msisdn);
                startActivity(intent);
                break;

            case R.id.imageViewGift:
                Toast.makeText(this, "Gift", Toast.LENGTH_SHORT).show();

                break;
        }

    }

    private void chatRequestDialog(String celeb_msisdn, String fan_msisdn, String name, String type) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Send chat request to " + name);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // here fan request for chat to celebrity
                requestForChat(celeb_msisdn, fan_msisdn, type);

            }
        });

        alertDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
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

                                String fan_msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
                                String room_name = celeb_msisdn + fan_msisdn;
                                Log.d("room_name", room_name);
//                                startActivity(new Intent(getApplicationContext(), ChatRoomActivity.class));
                                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);//ChatViewActivity
                                intent.putExtra("CELEB_PIC", profilePic);
                                intent.putExtra("CELEB_NAME", fbName);
                                intent.putExtra("room", room_name);
                                startActivity(intent);
                                //TastyToast.makeText(getApplicationContext(),"Start Chat Activity!",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);

//                                startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));

//                                intent.putExtra("imageUrl",profilePic);
//                                intent.putExtra("name",fbName);
//                                startActivity(intent);
                                TastyToast.makeText(getApplicationContext(), "Start Chat Activity!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                //startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));


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


}
