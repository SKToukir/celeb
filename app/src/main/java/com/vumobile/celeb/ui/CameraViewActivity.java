package com.vumobile.celeb.ui;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.CameraView;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.model.ServerPostRequest;
import com.vumobile.fan.login.Session;

import io.agora.rtc.Constants;

public class CameraViewActivity extends BaseActivity{

    private Button btnGoLive, btnPreschedule;
    private TextView txtCountTimer;
    private Camera mCamera = null;
    private CameraView mCameraView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        initUI();

        try{
            mCamera = Camera.open(1);//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        //btn to close the application
        Button imgGoLive = (Button)findViewById(R.id.btnGoLive);
        imgGoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startThreads();
            }
        });

        btnPreschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraViewActivity.this, SetScheduleActivity.class));
            }
        });
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void initUI() {
        btnPreschedule = (Button) findViewById(R.id.btnPreschedule);
        txtCountTimer = (TextView) findViewById(R.id.txtCountTimer);
    }

    private void startThreads() {

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtCountTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                TastyToast.makeText(getApplicationContext(),"Start Live",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
                CameraViewActivity.this.forwardToLiveRoom(Constants.CLIENT_ROLE_BROADCASTER);
            }
        }.start();


    }


    public void forwardToLiveRoom(int cRole) {

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        if (!msisdn.isEmpty() || msisdn != null || msisdn != "") {
            new ServerPostRequest().onLive(getApplicationContext(), msisdn, "1");
        }

        String room = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
        Intent i = new Intent(CameraViewActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
        i.putExtra("user", "celeb");
        startActivity(i);
        mCamera.release();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mCamera.release();
    }
}
