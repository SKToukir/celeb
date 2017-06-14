package com.vumobile.celeb.ui;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.CameraView;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.model.ServerPostRequest;
import com.vumobile.fan.login.Session;

import io.agora.rtc.Constants;

public class CameraViewActivity extends BaseActivity{

    private Button btnGoLive, btnPreschedule, imgGoLive;
    private ImageView txtCountTimer;
    private Camera mCamera = null;
    private CameraView mCameraView = null;

    Integer[] imageId = {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three
    };

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
        imgGoLive = (Button)findViewById(R.id.btnGoLive);
        imgGoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                btnGoLive.setVisibility(View.GONE);
//                btnPreschedule.setVisibility(View.GONE);
                startThreads();
            }
        });

        btnPreschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCamera.release();
                Intent intent = new Intent(CameraViewActivity.this, SetScheduleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("live","3");
                startActivity(intent);
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
        txtCountTimer = (ImageView) findViewById(R.id.txtCountTimer);
    }

    private void startThreads() {

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                //txtCountTimer.setText("" + millisUntilFinished / 1000);
                int i = (int) (millisUntilFinished / 1000);
                txtCountTimer.setImageResource(imageId[i-1]);
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
