package com.vumobile.fan.login;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;

public class ImageOrVideoView extends AppCompatActivity {

    private ImageView imageViewMainPreview;
    private VideoView videoViewMainPlayer;
    ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_or_video_view);

        imageViewMainPreview = (ImageView) findViewById(R.id.imageViewMainPreview);
        videoViewMainPlayer = (VideoView) findViewById(R.id.videoViewMainPlayer);

        imageViewMainPreview.setVisibility(View.GONE);
        videoViewMainPlayer.setVisibility(View.GONE);


        String imgOrVid = getIntent().getStringExtra("IMG_OR_VID");
        String imgOrVidUrl = getIntent().getStringExtra("IMG_OR_VID_URL");

        if (imgOrVid.equals("1")) {

            imageViewMainPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(imgOrVidUrl).into(imageViewMainPreview);


        } else if (imgOrVid.equals("2")) {

            videoViewMainPlayer.setVisibility(View.VISIBLE);

//            Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"); //Declare your url here.
            Uri uri = Uri.parse(imgOrVidUrl); //Declare your url here.
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoViewMainPlayer);
            videoViewMainPlayer.setMediaController(mediaController);
            videoViewMainPlayer.setVideoURI(uri);
            videoViewMainPlayer.requestFocus();
            videoViewMainPlayer.start();
            progDailog = ProgressDialog.show(this, "", "Loading ...", true);

            videoViewMainPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    progDailog.dismiss();
                    videoViewMainPlayer.start();
                }

            });

        }

        Log.d("vvvv", "onCreate: " + imgOrVidUrl);


    } // end of onCreate
}
