package com.vumobile.fan.login.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.serverrequest.AllVolleyInterfaces;
import com.vumobile.fan.login.serverrequest.MyVolleyRequest;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class Gifts extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gifts, container, false);
        //    linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);

        String url = "http://wap.shabox.mobi/sticker_app_server/default.aspx?&contentCode=NEW_CONTENT";
        MyVolleyRequest.getAllGifts(getActivity().getApplicationContext(), Request.Method.GET, url, new AllVolleyInterfaces.ResponseString() {
            @Override
            public void getResponse(String responseResult) {
                Log.d("gift", "getResponse: " + responseResult);
            }

            @Override
            public void getResponseErr(String responseResultErr) {
                Log.d("gift", "getResponseErr: " + responseResultErr);
            }
        });

        return rootView;
    }

}
