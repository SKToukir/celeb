package com.vumobile.videocall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

/**
 * Created by toukirul on 18/5/2017.
 */

public class ViaVideoCall extends BaseActivity implements SinchService.StartFailedListener {

    private ProgressDialog mSpinner;
    private String celeb_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String user_name = getIntent().getStringExtra("fan_name");
        celeb_name = getIntent().getStringExtra("celeb_name");
        loginClicked(user_name);
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    private void loginClicked(String user_name) {
        String userName =user_name;

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
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
        openPlaceCallActivity();
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(getApplicationContext(), PlaceCallActivity.class);
        mainActivity.putExtra("call_name",celeb_name);
        startActivity(mainActivity);
    }
}
