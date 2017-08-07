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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.AndroidMultiPartEntity;
import com.vumobile.celeb.Utils.ScalingUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout imgVdoLayout;
    private Toolbar toolbar;
    private Button btnPost, btnClose, btnChoose;
    private EditText etEditPost;
    private ImageView imgPost;
    private VideoView vdoPost;
    private String postId, post, isImage, postUrls;
    public static final int IMAGE_PICKER_SELECT = 1;
    private Uri uri;
    private String filePath = null;
    private MediaController mediaControls;
    private ProgressBar progressBar;
    private TextView txtPercentage;
    long totalSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_posts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        postId = getIntent().getStringExtra("id");
        post = getIntent().getStringExtra("post");
        postUrls = getIntent().getStringExtra("post_urls");
        isImage = getIntent().getStringExtra("isImage");



        Log.d("extras", postId + " " + post + " " + postUrls + " " + isImage);

        initUI();
    }

    private void initUI() {

        imgVdoLayout = (RelativeLayout) findViewById(R.id.imgVdoLayout);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.prog);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnClose = (Button) findViewById(R.id.btn_close_edit);
        etEditPost = (EditText) findViewById(R.id.etEditPost);
        imgPost = (ImageView) findViewById(R.id.imgEdit);
        vdoPost = (VideoView) findViewById(R.id.vdoViewEdit);
        btnChoose = (Button) findViewById(R.id.btnChoose);

        btnPost.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        etEditPost.setOnClickListener(this);
        imgPost.setOnClickListener(this);
        vdoPost.setOnClickListener(this);
        btnChoose.setOnClickListener(this);

        etEditPost.setText(post);

        if (!postUrls.equals("")){
            imgVdoLayout.setVisibility(View.VISIBLE);
            btnChoose.setVisibility(View.VISIBLE);
        }

        if (isImage.equals("1")) {
            vdoPost.setVisibility(View.GONE);
            imgPost.setVisibility(View.VISIBLE);
            setImage(postUrls);
        } else if (isImage.equals("2")) {
            vdoPost.setVisibility(View.VISIBLE);
            imgPost.setVisibility(View.GONE);
            setVideo(postUrls);
        }
    }

    private void setVideo(String postUrls) {
        Uri uri = Uri.parse(postUrls); //Declare your url here.
        vdoPost.setVideoURI(uri);
        vdoPost.start();
        vdoPost.seekTo(3000);
        vdoPost.pause();
    }

    private void setImage(String postUrls) {

        try {
            Picasso.with(getApplicationContext()).load(postUrls).into(imgPost);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnPost:

                progressBar.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.GONE);
                btnChoose.setVisibility(View.GONE);
                btnPost.setVisibility(View.GONE);

                String comment = etEditPost.getText().toString();
                if (filePath == null || filePath.equals(null) || filePath.equals("")) {
                    String change = "0";
                    // if there is no change on image or video
                    postEdit(Api.URL_EDIT_POST, postId, comment, isImage, postUrls, change);
                    //new UploadFileToServer().execute(postId,comment,isimage,postUrls,change);
                } else {
                    String change = "1";
                    new UploadFileToServer().execute(postId, comment, isImage, filePath, change);
                }
                break;
            case R.id.btn_close_edit:
                imgPost.setImageResource(0);
                vdoPost.setVideoURI(null);
                imgPost.setVisibility(View.GONE);
                vdoPost.setVisibility(View.GONE);
                break;
            case R.id.etEditPost:
                Log.d("filepath", "l" + filePath);
                Log.d("filepath", "l" + postUrls);
                break;
            case R.id.imgEdit:
                break;
            case R.id.vdoViewEdit:
                break;
            case R.id.btnChoose:
                choose_from_gallery();
                break;

        }
    }


    private void postEdit(String editPost, String postId, String comment, String isimage, String postUrls, String urlEditPost) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, editPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String log = object.getString("result");
                            showAlert(log);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FromServer", "" + error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Change", urlEditPost);
                Log.d("whatthehell",urlEditPost);
                params.put("post", comment);
                Log.d("whatthehell",comment);
                params.put("postId", postId);
                Log.d("whatthehell",postId);
                params.put("IsImage", isimage);
                Log.d("whatthehell",isimage);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void choose_from_gallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        imgVdoLayout.setVisibility(View.VISIBLE);

        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();

            if (selectedMediaUri.toString().contains("images")) {
                postUrls = "null";
                //handle image
                Toast.makeText(getApplicationContext(), "This is a image", Toast.LENGTH_LONG).show();
                uri = data.getData();
                filePath = decodeFile(getRealPathFromURI(EditPostActivity.this, uri));
                decodeFile(filePath);
                imgPost.setImageResource(0);
                vdoPost.setVideoURI(null);
                imgPost.setVisibility(View.VISIBLE);
                vdoPost.setVisibility(View.GONE);
                previewMedia("1", filePath);

            } else if (selectedMediaUri.toString().contains("video")) {
                postUrls = "null";
                //handle video
                Toast.makeText(getApplicationContext(), "This is a video", Toast.LENGTH_LONG).show();

                Uri uri = data.getData();
                filePath = getRealPathFromURI(getApplicationContext(), uri);
                previewMedia("2", filePath);

            }

        }
    }

    private void previewMedia(String isimage, String filePath) {

        if (isimage.equals("1")) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            imgPost.setImageBitmap(bitmap);
        } else {
            if (mediaControls == null) {
                mediaControls = new MediaController(EditPostActivity.this);
            }

            imgPost.setVisibility(View.GONE);
            vdoPost.setVisibility(View.VISIBLE);
            vdoPost.setVideoPath(filePath);
            vdoPost.setMediaController(mediaControls);
            // start playing
            vdoPost.start();
        }

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
            String postId = params[0];
            String comment = params[1];
            String isImage = params[2];
            String filePath = params[3];
            String change = params[4];

            return uploadFile(postId, comment, isImage, filePath, change);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String postId, String comment, String isImage, String filePath, String change) {
            String responseString = null;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Api.URL_EDIT_POST);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                Log.d("data", postId + " " + comment + " " + isImage + " " + filePath + " " + change);

                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("IsImage", new StringBody(isImage));
                entity.addPart("postId", new StringBody(postId));
                entity.addPart("Change", new StringBody(change));
                entity.addPart("post", new StringBody(comment));
                // entity.addPart("Name",new StringBody("my name"));
                Log.d("Image", sourceFile.toString());

                totalSize = entity.getContentLength();
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

    //
//        /**
//         * Method to show alert dialog
//         */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
