package com.vumobile.celeb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.vumobile.celeb.R;

public class GaleeryActivityCeleb extends AppCompatActivity implements View.OnClickListener {
    private ImageView back, home;
    Toolbar toolbar;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeery_celeb);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        initUI();
    }

    private void initUI() {

        back = (ImageView) toolbar.findViewById(R.id.backCelebGallery);
        home = (ImageView) toolbar.findViewById(R.id.home);
        back.setOnClickListener(this);
        home.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.home:
                intent = new Intent(GaleeryActivityCeleb.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
            case R.id.backCelebGallery:
                intent = new Intent(GaleeryActivityCeleb.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;

        }
    }
}
