package com.vumobile.fan.login;

import android.content.Intent;
import android.os.Bundle;

import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.ui.BaseActivity;
import com.vumobile.celeb.ui.LiveRoomActivity;

import io.agora.rtc.Constants;

/**
 * Created by IT-10 on 5/15/2017.
 */

public class ViaLive extends BaseActivity {
    String celebFbName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        celebFbName = getIntent().getStringExtra("CELEB_FB_NAME");

        forwardToLiveRoom(Constants.CLIENT_ROLE_AUDIENCE);

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
        String room = celebFbName;
        Intent i = new Intent(getApplicationContext(), LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
        i.putExtra("user", "fan");
        startActivity(i);
        finish();
    }


}
