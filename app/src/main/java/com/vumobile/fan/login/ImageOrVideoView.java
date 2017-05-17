package com.vumobile.fan.login;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vumobile.celeb.R;

import java.io.File;

public class ImageOrVideoView extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewMainPreview, imageViewDownloadImageOrVideo;
    private VideoView videoViewMainPlayer;
    ProgressDialog progDailog;
    String imgOrVid, imgOrVidUrl;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_or_video_view);

        imageViewMainPreview = (ImageView) findViewById(R.id.imageViewMainPreview);
        imageViewDownloadImageOrVideo = (ImageView) findViewById(R.id.imageViewDownloadImageOrVideo);
        imageViewDownloadImageOrVideo.setOnClickListener(this);
        videoViewMainPlayer = (VideoView) findViewById(R.id.videoViewMainPlayer);

        imageViewMainPreview.setVisibility(View.GONE);
        videoViewMainPlayer.setVisibility(View.GONE);

        imgOrVid = getIntent().getStringExtra("IMG_OR_VID");
        imgOrVidUrl = getIntent().getStringExtra("IMG_OR_VID_URL");

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewDownloadImageOrVideo:
                downloadManagerToDownloadImageAndVideo(ImageOrVideoView.this, imgOrVidUrl);
                break;
        }
    }

    public void downloadManagerToDownloadImageAndVideo(Context context, String url) {

        String fileName;

        if (url.length() == 10) {
            fileName = url;
        } else if (url.length() > 10) {
            fileName = url.substring(url.length() - 10);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("Word has less than 10 characters!");
        }

        File direct = new File(Environment.getExternalStorageDirectory() + "/CelebApp");
        File dFile = new File(Environment.getExternalStorageDirectory() + "/CelebApp/" + fileName);

        if (!direct.exists()) {
            direct.mkdirs();
        }

        if (!dFile.exists()) {

            DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Demo")
                    .setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir("/CelebApp", fileName);

            mgr.enqueue(request);

            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show();
                        try {
                            unregisterReceiver(receiver);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Already downloaded", Toast.LENGTH_SHORT).show();
        }
    }


}
