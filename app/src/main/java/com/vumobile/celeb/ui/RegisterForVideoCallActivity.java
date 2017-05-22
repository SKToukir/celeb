package com.vumobile.celeb.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;
import com.vumobile.videocall.SinchService;

public class RegisterForVideoCallActivity extends com.vumobile.videocall.BaseActivity implements SinchService.StartFailedListener {
    private ProgressDialog mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_video_call);
    }

    public void btnRegisterForVideoCall(View view) {

        if (!getSinchServiceInterface().isStarted()) {
            SinchService.uName = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);
            Log.d("sssssssss",Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME));
            startService(new Intent(RegisterForVideoCallActivity.this, SinchService.class));
            getSinchServiceInterface().startClient(Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME));
            showSpinner();
        } else {
            Intent intent = new Intent(RegisterForVideoCallActivity.this,SinchService.class);
            SinchService.uName = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);
            startService(intent);
        }

    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }


    @Override
    public void onStarted() {

    }
    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

}
