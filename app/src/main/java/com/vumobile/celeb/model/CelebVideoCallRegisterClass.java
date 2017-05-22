package com.vumobile.celeb.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;
import com.vumobile.fan.login.Session;
import com.vumobile.videocall.BaseActivity;
import com.vumobile.videocall.SinchService;

/**
 * Created by toukirul on 21/5/2017.
 */

public class CelebVideoCallRegisterClass extends BaseActivity implements SinchService.StartFailedListener{

    String uName;
    Context context;

    public CelebVideoCallRegisterClass(Context mContext,String uName){
        startService(new Intent(this, SinchService.class));
        this.context = mContext;
        this.uName = uName;
        if (uName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }


        if (!getSinchServiceInterface().isStarted()) {
            this.uName = Session.retreiveFbName(context,Session.FB_PROFILE_NAME);
            getSinchServiceInterface().startClient(uName);
            Log.d("CelebRegisterForVideo",uName);
        }
    }

    @Override
    protected void onServiceConnected() {
        this.uName = Session.retreiveFbName(context,Session.FB_PROFILE_NAME);
        getSinchServiceInterface().setStartListener(this);
        Log.d("CelebRegisterForVideo",uName);
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {
        this.uName = Session.retreiveFbName(context,Session.FB_PROFILE_NAME);
        Log.d("CelebRegisterForVideo",uName);
    }
}
