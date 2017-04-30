package com.vumobile.fan.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.ui.BaseActivity;
import com.vumobile.celeb.ui.LiveRoomActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;

public class FanCelebProfileActivity extends BaseActivity implements View.OnClickListener {

    String msisdn, name, fbName, profilePic;

    ImageView imageViewNotification, imageViewMessage, imageViewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_celeb_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        imageViewNotification = (ImageView) toolbar.findViewById(R.id.imageViewNotification);
        imageViewMessage = (ImageView) toolbar.findViewById(R.id.imageViewMessage);
        imageViewHome = (ImageView) toolbar.findViewById(R.id.imageViewHome);
        imageViewNotification.setOnClickListener(this);
        imageViewMessage.setOnClickListener(this);
        imageViewHome.setOnClickListener(this);

        CircleImageView imageViewProfilePicFan = (CircleImageView) findViewById(R.id.imageViewProfilePicFan);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);

        Intent intent = getIntent();
        msisdn = intent.getStringExtra("msisdn");
        name = intent.getStringExtra("name");
        fbName = intent.getStringExtra("fbname");
        profilePic = intent.getStringExtra("profilePic");

        Picasso.with(getApplicationContext()).load(profilePic).into(imageViewProfilePicFan);
        textViewName.setText(fbName);


    } // end of onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageViewNotification:
                Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageViewMessage:
                Toast.makeText(this, "sms", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageViewHome:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_celeb) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    public void btnShowLive(View view) {
        FanCelebProfileActivity.this.forwardToLiveRoom(Constants.CLIENT_ROLE_AUDIENCE);
    }

    public void forwardToLiveRoom(int cRole) {

        // here put the room name
        //roomname@ which room user wants to join
        //String room = msisdn;
        String room = fbName;
        Intent i = new Intent(FanCelebProfileActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
        i.putExtra("user", "fan");
        startActivity(i);
    }


}
