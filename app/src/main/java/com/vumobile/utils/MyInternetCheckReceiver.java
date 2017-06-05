package com.vumobile.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by IT-10 on 6/4/2017.
 */

public class MyInternetCheckReceiver extends BroadcastReceiver {

    static Snackbar snackbar;
    static View mView;

    public MyInternetCheckReceiver() {}

    public MyInternetCheckReceiver(View view) {
        this.mView = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("internet 3", "onReceive: MyInternetCheckReceiver ");
        try {
            isNetworkAvailableShowSnackbar(context, mView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void isNetworkAvailableShowSnackbar(Context context, View view) {
        if (!isNetworkAvailable(context)) {
            snackbar = Snackbar.make(view, "Internet connection unavailable.", Snackbar.LENGTH_INDEFINITE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        } else {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
