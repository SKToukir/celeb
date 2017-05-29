package com.vumobile.fan.login.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vumobile.celeb.R;

/**
 * Created by IT-10 on 5/28/2017.
 */

public class Credits extends Fragment {
    LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_credits, container, false);
        //    linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);
        return rootView;
    }

}
