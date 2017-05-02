package com.vumobile.celeb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;

public class FBPostActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgCelebImage;
    private TextView txtCelebName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbpost);

        initUI();


    }

    private void initUI() {
        txtCelebName = (TextView) findViewById(R.id.txtCelebName);
        imgCelebImage = (ImageView) findViewById(R.id.imgCelebImage);

        Picasso.with(getApplicationContext()).load(Session.retreivePFUrl(getApplicationContext(),Session.FB_PROFILE_PIC_URL)).into(imgCelebImage);
        txtCelebName.setText(Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

        }
    }
}
