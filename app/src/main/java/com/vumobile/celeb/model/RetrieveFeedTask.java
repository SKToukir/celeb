package com.vumobile.celeb.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by toukirul on 16/7/2017.
 */

public class RetrieveFeedTask extends AsyncTask<String,Void,Bitmap> {

    ShareDialog shareDialog;
    Context context;

    public RetrieveFeedTask(Context context){
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {


        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        shareDialog = new ShareDialog((Activity) context);
    }
}
