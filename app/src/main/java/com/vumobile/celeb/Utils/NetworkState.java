package com.vumobile.celeb.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.util.Log;

import com.github.pwittchen.reactivewifi.ReactiveWifi;
import com.github.pwittchen.reactivewifi.WifiSignalLevel;
import com.vumobile.celeb.ui.LiveRoomActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by toukirul on 3/5/2017.
 */

public class NetworkState {

    Context context;
    String signal;

    public NetworkState(Context context) {
        this.context = context;
    }

    public int haveNetworkConnection() {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    Log.d("Connected:", "wifi");
//                    getWifiSignal();
                    getLinkRate();
                    return 1;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    Log.d("Connected:", "Mobile 3g");
                    return 0;
                }
            }
        }
        if (haveConnectedWifi == false && haveConnectedMobile == false) {

            //do something to handle if wifi & mobiledata is disabled

        } else {
            //do something else..
        }

        return 01;
    }

    public void getWifiSignal() {

        ReactiveWifi.observeWifiSignalLevel(context)
                .subscribeOn(Schedulers.io())
                // anything else what you can do with RxJava
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WifiSignalLevel>() {
                    @Override
                    public void call(WifiSignalLevel signalLevel) {
                        // do something with signalLevel
                        Log.d("signal", signalLevel.toString());

                        LiveRoomActivity.signal = signalLevel.toString();
                    }
                });
    }

    public void getLinkRate(){
        ReactiveWifi.observeWifiAccessPointChanges(context)
                .subscribeOn(Schedulers.io())
                // anything else what you can do with RxJava
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WifiInfo>() {
                    @Override public void call(WifiInfo wifiInfo) {
                        // do something with wifiInfo
                        Log.d("signal",wifiInfo.toString());
                        LiveRoomActivity.linkRate = String.valueOf(wifiInfo.getLinkSpeed());
                    }
                });
    }
}
