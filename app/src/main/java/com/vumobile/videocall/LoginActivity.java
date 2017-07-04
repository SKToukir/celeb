package com.vumobile.videocall;

/**
 * Created by toukirul on 18/5/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.celeb.R;
import com.vumobile.fan.login.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private ImageView imgCalleImage;
    private Button mLoginButton, btnSentVideoRequest;
    private EditText mLoginName;
    private ProgressDialog mSpinner;
    private TextView txtUserName;
    static String user_name, celeb_profilePic, celeb_msisdn, celeb_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        user_name = getIntent().getStringExtra("fan_name");
        celeb_name = getIntent().getStringExtra("celeb_name");
        celeb_profilePic = getIntent().getStringExtra("profilePic");
        celeb_msisdn = getIntent().getStringExtra("celeb_msisdn");


        btnSentVideoRequest = (Button) findViewById(R.id.btnSentVideoRequest);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        mLoginName = (EditText) findViewById(R.id.loginName);
        txtUserName.setText(celeb_name);
        mLoginName.setText(user_name);

        imgCalleImage = (ImageView) findViewById(R.id.imgCalleImage);
        Picasso.with(getApplicationContext()).load(celeb_profilePic).into(imgCalleImage);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        //loginClicked(user_name);
        // here login button is GO button
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                loginClicked(user_name);
            }
        });


        // sent request for video calling to celebrity
        btnSentVideoRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String fan_msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

                requestForVideo(celeb_msisdn, fan_msisdn, "2");

            }
        });
    }

    @Override
    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
       // openPlaceCallActivity(celeb_name);
        if (!getSinchServiceInterface().isStarted()) {
            SinchService.uName = user_name;
            startService(new Intent(LoginActivity.this, SinchService.class));
            getSinchServiceInterface().startClient(user_name);
            Log.d("SSSSSSSS","Sinch service started");
        } else {
            Intent intent = new Intent(LoginActivity.this,SinchService.class);
            SinchService.uName = user_name;
            startService(intent);
            Log.d("SSSSSSSS","Sinch service started else");
        }
    }

    public void loginClicked(String user_names) {

        String userName = user_names;

        if (userName.isEmpty()) {
            Toast.makeText(this, "Nmae Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            SinchService.uName = userName;
            startService(new Intent(LoginActivity.this, SinchService.class));
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            Intent intent = new Intent(LoginActivity.this,SinchService.class);
            SinchService.uName = userName;
            startService(intent);
            openPlaceCallActivity(celeb_name);
        }

//        if (!getSinchServiceInterface().isStarted()) {
//            getSinchServiceInterface().startClient(user_names);
//            showSpinner();
//        } else {
//            openPlaceCallActivity(celeb_name);
//        }
    }

    private void openPlaceCallActivity(String celeb_name) {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        mainActivity.putExtra("call_name", celeb_name);
        mainActivity.putExtra("celeb_profilePic", celeb_profilePic);
        startActivity(mainActivity);
        finish();
    }

    private void showSpinner() {

        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();

        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mSpinner.dismiss();
                }
            }
        };thread.start();

    }

    private void requestForVideo(String celeb_msisdn, String fan_msisdn, String type) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_CHAT_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());


                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            String request_status = obj.getString("result").replaceAll(" ", "_");
                            Log.d("FromServer", request_status);


                            if (request_status.matches("Request_Pending") || request_status.equals("Request_Pending")) {
                                TastyToast.makeText(getApplicationContext(), "Your request is pending", TastyToast.LENGTH_LONG, TastyToast.INFO);

                            } else if (request_status.matches("Accepted")) {

                                mLoginButton.setVisibility(View.VISIBLE);
                                btnSentVideoRequest.setVisibility(View.GONE);


//                                String fan_msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
//                                String room_name = celeb_msisdn + fan_msisdn;
//                                Log.d("room_name", room_name);
////                                startActivity(new Intent(getApplicationContext(), ChatRoomActivity.class));
//                                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
//                                intent.putExtra("room", room_name);
//
//                                startActivity(intent);
//                                //TastyToast.makeText(getApplicationContext(),"Start Chat Activity!",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
//
////                                startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));
//
////                                intent.putExtra("imageUrl",profilePic);
////                                intent.putExtra("name",fbName);
////                                startActivity(intent);
//                                TastyToast.makeText(getApplicationContext(), "Start Chat Activity!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//                                //startActivity(new Intent(getApplicationContext(), ChatViewActivity.class));


                            }


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

                /*
                *  request flag = 1 means it is a chat request
                *  request flag = 2 means it is a video request
                * */

                Map<String, String> params = new HashMap<String, String>();
                params.put("Fan", fan_msisdn);
                params.put("Celebrity", celeb_msisdn);
                params.put("RequestType", type);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}