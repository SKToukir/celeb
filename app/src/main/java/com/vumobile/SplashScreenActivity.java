package com.vumobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.LogInAcitvity;

public class SplashScreenActivity extends AppCompatActivity {

    protected boolean mbActive;
    private ImageView splashImage;
    protected static final int TIMER_RUNTIME = 10; // in ms --> 10s

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        // here check network connection then caqll this method
        initUI();
    }

    private void initUI(){

    splashImage = (ImageView) findViewById(R.id.splashImage);
    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(splashImage);
    Glide.with(this).load(R.drawable.splash).into(imageViewTarget);
    final Thread timerThread = new Thread() {
        @Override
        public void run() {
            mbActive = true;
            try {
                sleep(14000);
//                int waited = 0;
//                while (mbActive && (waited < TIMER_RUNTIME)) {
//                    sleep(2100);
//                    if (mbActive) {
//                        waited += 2;
//                    }
//                }
            } catch (InterruptedException e) {
                // do nothing
            } finally {
                onContinue();
            }
        }
    };
    timerThread.start();
}

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        overridePendingTransition(0, 0);
    }

    public void onContinue() {
        // perform any final actions here

        runOnUiThread(new Runnable() {

            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,
                        LogInAcitvity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
