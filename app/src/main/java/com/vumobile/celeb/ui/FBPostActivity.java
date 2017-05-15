package com.vumobile.celeb.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.AndroidMultiPartEntity;
import com.vumobile.celeb.Utils.ScalingUtilities;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.model.ServerPostRequest;
import com.vumobile.fan.login.Session;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.agora.rtc.Constants;

public class FBPostActivity extends BaseActivity implements View.OnClickListener {

    private ShareDialog shareDialog;
    private RelativeLayout imgVdoLayout;
    private LinearLayout selectImageVideoLayout;
    private ImageView imgCelebImage;
    private TextView txtCelebName;
    private Button btnGoLive, btnGetPhotoVideo, btn_close, btn_edit;
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
    private ImageView btnBack, btnHome;
    private Toolbar toolbar;
    private Intent intent;
    public String celebComment;
    private EditText etComment;
    private String name, msisdn, celebID,gender,flags_notification,image_url;
    Uri uri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_fbpost);
        toolbar = (Toolbar) findViewById(R.id.toolBar_post);
        setSupportActionBar(toolbar);

        initUI();

        Intent intent = getIntent();
        celebID = intent.getStringExtra("celeb_id");
        gender = intent.getStringExtra("gender");
        image_url = intent.getStringExtra("image_url");
        name = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);
        msisdn = Session.retreivePhone(getApplicationContext(),Session.USER_PHONE);


    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void initUI() {

        imgVdoLayout = (RelativeLayout) findViewById(R.id.imgVdoLayout);
        selectImageVideoLayout = (LinearLayout) findViewById(R.id.selectImageVideoLayout);

        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        etComment = (EditText) findViewById(R.id.etWhatsYourMind);
        etComment.setOnClickListener(this);
        btnBack = (ImageView) toolbar.findViewById(R.id.btn_back);
        btnHome = (ImageView) toolbar.findViewById(R.id.btnHome);
        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);

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


        Picasso.with(getApplicationContext()).load(Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL)).into(imgCelebImage);
        txtCelebName.setText(Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGoLive:
                FBPostActivity.this.forwardToLiveRoom(Constants.CLIENT_ROLE_BROADCASTER);
                break;
            case R.id.btnGetPhotoVideo:
                choose_from_gallery();
                break;
            case R.id.btnPost:
                celebComment = etComment.getText().toString();

                new UploadFileToServer().execute(filePath,celebComment);

                break;
            case R.id.btn_back:
                intent = new Intent(FBPostActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btnHome:
                intent = new Intent(FBPostActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_close:
                imgPreview.setImageResource(0);
                vdoPreview.setVideoURI(null);
                imgVdoLayout.setVisibility(View.GONE);
                selectImageVideoLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_edit:
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
                Toast.makeText(getApplicationContext(), "This is a image", Toast.LENGTH_LONG).show();
                isImage = true;
                uri = data.getData();
                filePath = decodeFile(getRealPathFromURI(FBPostActivity.this, uri));
                decodeFile(filePath);
                selectImageVideoLayout.setVisibility(View.GONE);
                imgVdoLayout.setVisibility(View.VISIBLE);
                previewMedia(isImage, filePath);

            } else if (selectedMediaUri.toString().contains("video")) {
                selectImageVideoLayout.setVisibility(View.GONE);
                imgVdoLayout.setVisibility(View.VISIBLE);
                //handle video
                Toast.makeText(getApplicationContext(), "This is a video", Toast.LENGTH_LONG).show();
                isImage = false;
                Uri uri = data.getData();
                filePath = getRealPathFromURI(getApplicationContext(), uri);
                previewMedia(isImage, filePath);

            }

        }
    }

    private void previewMedia(boolean isImage, String s) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vdoPreview.setVisibility(View.GONE);
//            // bimatp factory
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            // down sizing image as it throws OutOfMemory Exception for larger
//            // images
//            options.inSampleSize = 8;
//
//            final Bitmap bitmap = BitmapFactory.decodeFile(s, options);
//
//            imageBitmap = bitmap;
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            imgPreview.setImageBitmap(bitmap);
            //rotate(rotateDegree);
        } else {
            imgPreview.setVisibility(View.GONE);
            vdoPreview.setVisibility(View.VISIBLE);
            vdoPreview.setVideoPath(s);
            // start playing
            vdoPreview.pause();
        }
    }

    public void forwardToLiveRoom(int cRole) {

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        if (!msisdn.isEmpty() || msisdn != null || msisdn != "") {
            new ServerPostRequest().onLive(getApplicationContext(), msisdn, "1");
        }
        // here put the room name
        //roomname@ which room user wants to join
        //String room = msisdn;
        String room = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
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
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
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
     */
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
            String comment = params[1];

            return uploadFile(fPath,comment);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String fPath,String cmnt) {
            String responseString = null;


            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://vumobile.biz/Toukir/sendFileToServer.php");
            HttpPost httppost = new HttpPost("http://wap.shabox.mobi/testwebapi/Notification/up?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre");

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
                Log.d("data",name+" "+msisdn+" "+celebID+" "+gender+" "+IsImage(isImage)+" "+image_url+" "+flags_notification+" "+cmnt);

                entity.addPart("image", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
                entity.addPart("Name",
                        new StringBody(name));
                entity.addPart("MSISDN", new StringBody(msisdn));
                entity.addPart("Celeb_id", new StringBody(celebID));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("IsImage", new StringBody(IsImage(isImage)));
                entity.addPart("Image_url", new StringBody(image_url));
                // set flag notification for post is 2
                // and here all notification is post
                entity.addPart("Flags_Notificaton", new StringBody("2"));
                entity.addPart("post", new StringBody(cmnt));
               // entity.addPart("Name",new StringBody("my name"));
                Log.d("Image",sourceFile.toString());
//                entity.addPart("Name",new StringBody(name));
//                entity.addPart("MSISDN",new StringBody(msisdn));
//                entity.addPart("Celeb_id",new StringBody(celebID));
//                entity.addPart("gender",new StringBody(gender));
//                entity.addPart("IsImage",new StringBody(String.valueOf(isImage)));
//                entity.addPart("Image_url",new StringBody(String.valueOf(image_url)));
//                entity.addPart("Flags_Notificaton",new StringBody("2"));
//                entity.addPart("post",new StringBody(cmnt));

                totalSize = entity.getContentLength();
                //httppost.setHeader("Content-Type", "multipart/form-data");
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

    private String IsImage(boolean isImage) {

        if (isImage){
            return "1";
        }else {
            return "2";
        }
    }

    /**
     * Method to show alert dialog
     */
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

    private void sharePhoto(Bitmap img, String cmnt) {
        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        Bitmap image = img;

        shareDialog = new ShareDialog(this);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(cmnt)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();


        shareDialog.show(content);
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
