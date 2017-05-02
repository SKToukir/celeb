package com.vumobile.celeb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.model.ServerPostRequest;
import com.vumobile.fan.login.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.agora.rtc.Constants;

@SuppressWarnings("ALL")
public class CelebHomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ImageView profilePictureView, imgGoLive, imgPic;
    NavigationView navigationView;
    TextView txtProfileName, txtCele;
    EditText etWhatsYourMind;
    String celebName,msisdn,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celeb_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initUI();
        initNavHeaderView();

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        getCelebProfile(Api.URL_GET_SINGLE_CELEB+msisdn);


        // if can not access celeb name from session
        if (Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME)==null ||
                Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME)=="null"){
            txtCele.setText(celebName);
        }

    }

    private void getCelebProfile(String s) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, s, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer", jsonObject.toString());

                try {
                    JSONArray array = jsonObject.getJSONArray("result");

                    JSONObject obj = array.getJSONObject(0);
                    celebName = obj.getString("Name");
                    Log.d("FromServer",celebName);
                    msisdn = obj.getString("MSISDN");
                    Log.d("FromServer",msisdn);
                    imageUrl = obj.getString("Image_url");
                    Log.d("FromServer",imageUrl);

                    new Session().saveData(getApplicationContext(),celebName,msisdn,true,true,imageUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CelebHomeActivity.this);
        //Adding request to the queue
        requestQueue.add(request);
    }


    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void initUI() {

        etWhatsYourMind = (EditText) findViewById(R.id.etWhatsYourMind);
        txtCele = (TextView) findViewById(R.id.txtCelebName);
        imgPic = (ImageView) findViewById(R.id.celebImage);
        imgGoLive = (ImageView) findViewById(R.id.imgGoLive);
        imgGoLive.setOnClickListener(this);
        etWhatsYourMind.setOnClickListener(this);


        txtCele.setText(Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME));
        Picasso.with(getApplicationContext()).load(Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL)).into(imgPic);

    }

    private void initNavHeaderView() {


        txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewPfName);

        profilePictureView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewCeleb);

        txtProfileName.setText(Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME));
        Picasso.with(getApplicationContext()).load(Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL)).into(profilePictureView);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.celeb_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_gifts) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgGoLive:
                //startActivity(new Intent(CelebHomeActivity.this, CameraViewActivity.class));
                TastyToast.makeText(getApplicationContext(), "Start Live", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                CelebHomeActivity.this.forwardToLiveRoom(Constants.CLIENT_ROLE_BROADCASTER);
                break;
            case R.id.etWhatsYourMind:
                //TODO
                startActivity(new Intent(CelebHomeActivity.this,FBPostActivity.class));
                break;
        }
    }

    public void forwardToLiveRoom(int cRole) {

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        if (!msisdn.isEmpty() || msisdn != null || msisdn != "") {
            new ServerPostRequest().onLive(getApplicationContext(), msisdn, "1");
        }

        String room = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
        Intent i = new Intent(CelebHomeActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
        i.putExtra("user", "celeb");
        startActivity(i);

    }

    @Override
    protected void onResume() {
        new ServerPostRequest().onLive(getApplicationContext(), Session.retreivePhone(getApplicationContext(), Session.USER_PHONE), "0");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        new ServerPostRequest().onLive(getApplicationContext(), Session.retreivePhone(getApplicationContext(), Session.USER_PHONE), "0");
        super.onDestroy();
    }
}