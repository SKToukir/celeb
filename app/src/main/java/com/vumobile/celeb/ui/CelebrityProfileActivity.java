package com.vumobile.celeb.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.vumobile.Config.Api;
import com.vumobile.ParentActivity;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.fan.login.Session;
import com.vumobile.service.MyFirebaseInstanceIDService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CelebrityProfileActivity extends BaseActivity implements View.OnClickListener {

    private static String profile_url = "", fbName;
    private String regId;
    private Button btnGoLive;
    public static String DEVICE_TOKEN = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private String firstName, lastName, email = "null", birthday, gender, loc;
    private URL profilePicture;
    private static String userId;
    private TextView txtStatus, txtConfirmationMessage;
    private ImageView imgProfilePic;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_celebrity_profile);
        startService(new Intent(CelebrityProfileActivity.this, MyFirebaseInstanceIDService.class));
        notificationRegister();

        if (Session.isReg(getApplicationContext(), Session.REGISTERED_CELEB) == false) {
            initUI();
            initializeControls();
            loginWithFb();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    isRegistered();
                }
            });

            thread.start();

            if (profile_url != "") {
                Log.d("profile_url", "Url " + Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL));
                txtStatus.setText(Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME));
                Picasso.with(getApplicationContext()).load(Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL)).into(imgProfilePic);
                txtConfirmationMessage.setVisibility(View.VISIBLE);
            }
        } else {
            new Session().saveCelebState(getApplicationContext(), true);
            startActivity(new Intent(CelebrityProfileActivity.this, CelebHomeActivity.class));
            finish();
        }

    }

    // retreive device token for push notification
    private void notificationRegister() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Api.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Api.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Api.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String time = intent.getStringExtra("time_stamp");
                    String imageUrl = intent.getStringExtra("image_url");


                //    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Api.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
        Log.e("taggg", "Firebase:" + regId);
    }

    private void initializeControls() {
        txtConfirmationMessage = (TextView) findViewById(R.id.txtConfirmationMessage);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        loginButton.setReadPermissions("public_profile email publish_actions");


    }


    private void loginWithFb() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //txtStatus.setText(loginResult.getAccessToken().getToken());
                Log.d("permission", loginResult.getAccessToken().getToken());
                Log.d("statussss", loginResult.toString());
                txtConfirmationMessage.setVisibility(View.VISIBLE);
                callGraphApi(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                txtStatus.setText("Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                txtStatus.setText(error.getMessage());
            }
        });
    }


    private void callGraphApi(AccessToken token) {

        AccessToken access_toke = token;
        Log.e("lol", "profile" + token.toString());
        GraphRequest request = GraphRequest.newMeRequest(access_toke, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("lol", object.toString());
                Log.d("lol", response.toString());

                try {
                    userId = object.getString("id");
                    profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                    profile_url = "https://graph.facebook.com/" + userId + "/picture?width=500&height=500";
                    Log.d("profile_url", profile_url);
                    Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + userId + "/picture?width=500&height=500").into(imgProfilePic);
                    Log.e("lol", "profile" + profilePicture);
                    if (object.has("first_name"))
                        firstName = object.getString("first_name");
                    Log.e("lol", "first name" + firstName);
                    if (object.has("last_name"))
                        lastName = object.getString("last_name");
                    Log.e("lol", "last name" + lastName);
                    if (object.has("email"))
                        email = object.getString("email");
                    Log.e("lol", "email" + email);
                    if (object.has("birthday"))
                        birthday = object.getString("birthday");
                    Log.e("lol", "birthday" + birthday);
                    if (object.has("gender"))
                        gender = object.getString("gender");
                    Log.e("lol", "gender" + gender);

                    if (object.has("location"))
                        loc = object.getString("location");
                    Log.e("lol", "location" + loc);

                    fbName = firstName + " " + lastName;
                    new Session().saveProfilePicUrl(getApplicationContext(), profile_url, fbName);

                    if (!fbName.equals("null") || fbName != "null"){
                        txtStatus.setText(fbName);
                    }else {
                        txtStatus.setText("");
                        txtConfirmationMessage.setText("Please press Log out button and login again!");
                    }



                    sendDataToServer();

//                    Intent main = new Intent(MainActivity.this,Main2Activity.class);
//                    main.putExtra("name",firstName);
//                    main.putExtra("surname",lastName);
//                    main.putExtra("imageUrl",profilePicture.toString());
//                    startActivity(main);
//                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        //Here we put the requested fields to be returned from the JSONObject
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, birthday, first_name, last_name, email, gender, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void sendDataToServer() {

        boolean celebOrNot = new Session().isCeleb(CelebrityProfileActivity.this, Session.IS_CELEB);

        if (!celebOrNot) {

            // send data to fan table

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_SAVE_FAN_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("FromServer", response.toString());
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String successLog = jsonObj.getString("result");
                             //   TastyToast.makeText(getApplicationContext(), successLog, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                // user logged in with fb
                                new Session().saveFbLoginStatus(getApplicationContext(),true);
                                startActivity(new Intent(CelebrityProfileActivity.this, ParentActivity.class));
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("FromServer", "" + error.getMessage());
                        //    TastyToast.makeText(getApplicationContext(), "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Name", fbName);
                    params.put("UserName", Session.retreiveName(getApplicationContext(), Session.USER_NAME));
                    params.put("MSISDN", Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));
                    params.put("Celeb_id", userId);
                    params.put("gender", gender);
                    params.put("Image_url", profile_url);
                    params.put("email",email);
                    params.put("Fb_login_status", "1");
                    params.put("RegId", regId);
//                params.put("Flag", String.valueOf(celebOrNot));

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {

            // send data to celebrity table
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_SAVE_CELEB_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("FromServer", response.toString());
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String successLog = jsonObj.getString("result");
                            //    TastyToast.makeText(getApplicationContext(), successLog, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                // user logged in with fb
                                new Session().saveFbLoginStatus(getApplicationContext(),true);
                                if (!celebOrNot) {
                                    startActivity(new Intent(CelebrityProfileActivity.this, ParentActivity.class));
                                    finish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("FromServer", "" + error.getMessage());
                   //         TastyToast.makeText(getApplicationContext(), "Error!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Name", fbName);
                    params.put("UserName", Session.retreiveName(getApplicationContext(), Session.USER_NAME));
                    params.put("MSISDN", Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));
                    params.put("Celeb_id", userId);
                    params.put("gender", gender);
                    params.put("Image_url", profile_url);
                    params.put("Fb_login_status", "1");
                    params.put("RegId", regId);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("data", data.toString());
    }

    private void share() {
        shareDialog = new ShareDialog(this);
        List<String> taggedUserIds = new ArrayList<>();
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.sitepoint.com"))
                .setContentTitle("This is a content title")
                .setContentDescription("This is a description")
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#sitepoint").build())
                .setPeopleIds(taggedUserIds)
                .setPlaceId("{PLACE_ID}")
                .build();

        shareDialog.show(content);
    }

    private void sharePhoto() {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        shareDialog = new ShareDialog(this);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();


        shareDialog.show(content);

    }


    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void initUI() {
        btnGoLive = (Button) findViewById(R.id.btnGoLive);

        btnGoLive.setOnClickListener(this);
    }

    public void forwardToLiveRoom(int cRole) {

        String room = Session.retreiveName(CelebrityProfileActivity.this, Session.USER_NAME);

        Intent i = new Intent(CelebrityProfileActivity.this, LiveRoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CROLE, cRole);
        i.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);

        startActivity(i);
    }

    public boolean isRegistered() {

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        boolean check = false;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.URL_CHECK_CELEB_REG + msisdn, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("FromServer", response.toString());

                try {
                    String result = response.getString("result");

                    if (result.matches("1")) {
                        Log.d("FromServer", "confirm");
                        new Session().saveCelebState(getApplicationContext(), true);
                        startActivity(new Intent(CelebrityProfileActivity.this, CelebHomeActivity.class));
                        finish();
                    } else {
                        Log.d("FromServer", "not confirm");
                 //       TastyToast.makeText(getApplicationContext(), "Not Confirm yet!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        return check;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGoLive:
                startActivity(new Intent(CelebrityProfileActivity.this, CelebHomeActivity.class));
                finish();
                break;
        }

    }


}
