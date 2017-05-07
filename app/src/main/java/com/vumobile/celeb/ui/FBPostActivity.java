package com.vumobile.celeb.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.AndroidMultiPartEntity;
import com.vumobile.celeb.Utils.ConvertImageClass;
import com.vumobile.celeb.Utils.ScalingUtilities;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.fan.login.Session;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.agora.rtc.Constants;

public class FBPostActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imgCelebImage;
    private TextView txtCelebName;
    private Button btnGoLive,btnGetPhotoVideo;
    public static final int IMAGE_PICKER_SELECT = 1;
    private String filePath = null;
    private boolean isImage = true;
    private ImageView imgPreview;
    private VideoView vdoPreview;
    private Button btnPost;
    private Bitmap imageBitmap;
    private ProgressBar progressBar;
    long totalSize = 0;
    private TextView txtPercentage;
    float rotateDegree = 270f;


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbpost);


        initUI();


    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void initUI() {
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnPost = (Button) findViewById(R.id.btnPost);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vdoPreview = (VideoView) findViewById(R.id.videoPreview);
        txtCelebName = (TextView) findViewById(R.id.txtCelebName);
        imgCelebImage = (ImageView) findViewById(R.id.imgCelebImage);
        btnGetPhotoVideo = (Button) findViewById(R.id.btnGetPhotoVideo);
        btnGoLive = (Button) findViewById(R.id.btnGoLive);
        btnGoLive.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnGetPhotoVideo.setOnClickListener(this);

        Picasso.with(getApplicationContext()).load(Session.retreivePFUrl(getApplicationContext(),Session.FB_PROFILE_PIC_URL)).into(imgCelebImage);
        txtCelebName.setText(Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnGoLive:
                FBPostActivity.this.forwardToLiveRoom(Constants.CLIENT_ROLE_BROADCASTER);
                break;
            case R.id.btnGetPhotoVideo:
                choose_from_gallery();
                break;
            case R.id.btnPost:
                String img = new ConvertImageClass().baseImage(imageBitmap);
                Log.d("Image",img);
                new UploadFileToServer().execute(filePath);
                break;
        }
    }

    private void choose_from_gallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();

            if (selectedMediaUri.toString().contains("images")) {
                //handle image
                Toast.makeText(getApplicationContext(),"This is a image", Toast.LENGTH_LONG).show();
                isImage = true;
                Uri uri = data.getData();
                filePath = decodeFile(getRealPathFromURI(FBPostActivity.this,uri));
                decodeFile(filePath);
                previewMedia(isImage,decodeFile(filePath));

            } else if (selectedMediaUri.toString().contains("video")) {
                //handle video
                Toast.makeText(getApplicationContext(),"This is a video",Toast.LENGTH_LONG).show();
                isImage = false;
                Uri uri = data.getData();
                filePath = getRealPathFromURI(getApplicationContext(),uri);
                previewMedia(isImage, getRealPathFromURI(FBPostActivity.this,uri));

            }

        }
    }

    private void previewMedia(boolean isImage, String s) {
        // Checking whether captured media is image or video
        if (isImage) {
            //imgPreview.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(s, options);

            imageBitmap = bitmap;

            imgPreview.setImageBitmap(bitmap);
            rotate(rotateDegree);
        } else {
            //imgPreview.setVisibility(View.GONE);
            vdoPreview.setVisibility(View.VISIBLE);
            vdoPreview.setVideoPath(s);
            // start playing
            vdoPreview.start();
        }
    }

    public void forwardToLiveRoom(int cRole) {
        // here put the room name
        //roomname@ which room user wants to join
        //String room = msisdn;
        String room = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);
        Intent i = new Intent(FBPostActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
        i.putExtra("user", "celeb");
        startActivity(i);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Celebrity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create "
                        + "Celebrity" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    // this method call for get video file uri
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String decodeFile(String path) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, 80, 80, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= 800 && unscaledBitmap.getHeight() <= 800)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 80, 80, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/myTmpDir");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 5, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            txtPercentage.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... params) {
            String fPath = params[0];

            return uploadFile(fPath);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String fPath) {
            String responseString = null;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://wap.shabox.mobi/testwebapi/Notification/upload");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(fPath);

                // Adding file data to http body



//                entity.addPart("Name", new StringBody("name"));
//                entity.addPart("MSISDN", new StringBody("name"));
//                entity.addPart("celeb_id", new StringBody("name"));
//                entity.addPart("gender", new StringBody("name"));
//                entity.addPart("Image_url", new StringBody("name"));
                entity.addPart("Image", new FileBody(sourceFile));
//                // Extra parameters if you want to pass to server
//                entity.addPart("complain",new StringBody(complainText));
//                entity.addPart("isImage",new StringBody(String.valueOf(imageOrNot)));
//                entity.addPart("user_id", new StringBody(uId));
//                entity.addPart("user_location", new StringBody(loc.trim()));

                totalSize = entity.getContentLength();
                //httppost.setHeader("Content-Type", "application/json; charset=utf8");
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("FromServer", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        progressBar.setVisibility(View.GONE);
                        txtPercentage.setVisibility(View.GONE);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void rotate(float degree) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        imgPreview.startAnimation(rotateAnim);
    }

}
