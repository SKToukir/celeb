package com.vumobile.videocall;

/**
 * Created by toukirul on 18/5/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;

public class PlaceCallActivity extends BaseActivity {

    private Button mCallButton;
    private TextView mCallName;
    private String callName, cele_profile_pic;
    private ImageView imgCeleb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        imgCeleb = (ImageView) findViewById(R.id.imgCelebs);
        mCallName = (TextView) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        callName = getIntent().getStringExtra("call_name");
        cele_profile_pic = getIntent().getStringExtra("celeb_profilePic");
        Picasso.with(getApplicationContext()).load(cele_profile_pic).into(imgCeleb);

        Log.d("call_name",callName);

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        mCallName.setText(callName);
        //mCallName.setText(getSinchServiceInterface().getUserName());
        //userName.setText(getSinchServiceInterface().getUserName());
        userName.setText(callName);
        mCallButton.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {
        //mCallName.setText(callName);
        String userName = mCallName.getText().toString();
        Log.d("call_name",userName);
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        callScreen.putExtra("image",cele_profile_pic);
        startActivity(callScreen);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked();
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };

}
