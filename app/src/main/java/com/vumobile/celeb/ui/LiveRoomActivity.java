package com.vumobile.celeb.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cipherthinkers.shapeflyer.ShapeFlyer;
import com.cipherthinkers.shapeflyer.flyschool.FPoint;
import com.cipherthinkers.shapeflyer.flyschool.FlyBluePrint;
import com.cipherthinkers.shapeflyer.flyschool.FlyPath;
import com.cipherthinkers.shapeflyer.flyschool.PATHS;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vumobile.celeb.Adapters.CommentListAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.CommentClass;
import com.vumobile.celeb.Utils.Methods;
import com.vumobile.celeb.model.AGEventHandler;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.model.MyBounceInterpolator;
import com.vumobile.celeb.model.ServerPostRequest;
import com.vumobile.celeb.model.VideoStatusData;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.ui.fragment.Gifts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

@SuppressWarnings("ALL")
public class LiveRoomActivity extends BaseActivity implements AGEventHandler, View.OnClickListener {

    public int COUNT_GIFTS = 0;
    private LinearLayout gift_container, gift_price_container;
    static String likeRoomName, viewRoomName;
    private ShapeFlyer mShapeFlyer;
    private FrameLayout frameLayoutCommentGift;
    static String imageUrl;
    private FragmentTransaction ft;
    private FragmentManager fm;
    public static String signal = "";
    public static String linkRate = "";
    InputMethodManager imm;
    private int i = 5;
    private ImageView btnLike;
    public TextView txtLikes, txtViews;
    static TextView txtGiftsCount, txtTaka;
    private ImageView btnGift;
    static String temp_key, temp_key_view, temp_key_like, chat_user_name, comment_msisdn, user_name, id, op;
    String roomName;
    private static String video_id;
    private final static Logger log = LoggerFactory.getLogger(LiveRoomActivity.class);
    private GridVideoViewContainer mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;
    private Button btnSendComment;
    private List<CommentClass> commentClassList = new ArrayList<>();
    private ListView listOfComment;
    private EditText etComment;
    CommentListAdapter adapter;
    private String msisdn;
    private static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private static DatabaseReference root_like;
    private static DatabaseReference root_view;
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    Pattern pattern;
    Matcher m;
    public static int gift_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        initUI();
        // initialize comment list adapter
        adapter = new CommentListAdapter(this, R.layout.custom_comment_list, commentClassList);
        listOfComment.setAdapter(adapter);

        getRandomFlyer();

    }

    private void getRandomFlyer() {
        mShapeFlyer.addPath(PATHS.S_INVERTED_TOP_LEFT);
        mShapeFlyer.addPath(PATHS.S_BOTTOM_LEFT);
        mShapeFlyer.addPath(PATHS.S_INVERTED_BOTTOM_RIGHT);
        mShapeFlyer.addPath(PATHS.S_TOP_RIGHT);
        mShapeFlyer.addPath(PATHS.LINE_DIAGONAL_BOTTOM_LEFT);
        mShapeFlyer.addPath(PATHS.LINE_DIAGONAL_BOTTOM_RIGHT);
        mShapeFlyer.addPath(PATHS.LINE_DIAGONAL_TOP_LEFT);
        mShapeFlyer.addPath(PATHS.LINE_DIAGONAL_TOP_RIGHT);
        mShapeFlyer.addPath(PATHS.LINE_MIDDLE_TOP);
        mShapeFlyer.addPath(PATHS.LINE_MIDDLE_BOTTOM);
        mShapeFlyer.addPath(PATHS.LINE_MIDDLE_LEFT);
        mShapeFlyer.addPath(PATHS.LINE_MIDDLE_RIGHT);
        mShapeFlyer.addPath(new FlyBluePrint(new FPoint(0, 0),
                FlyPath.getMultipleLinePath(
                        new FPoint(0.1f, 0f),
                        new FPoint(0.1f, 0.1f),
                        new FPoint(0.2f, 0.1f),
                        new FPoint(0.2f, 0.2f),
                        new FPoint(0.3f, 0.2f),
                        new FPoint(0.3f, 0.3f),
                        new FPoint(0.4f, 0.3f),
                        new FPoint(0.4f, 0.4f),
                        new FPoint(0.5f, 0.4f),
                        new FPoint(0.5f, 0.5f),
                        new FPoint(0.6f, 0.5f),
                        new FPoint(0.6f, 0.6f),
                        new FPoint(0.7f, 0.6f),
                        new FPoint(0.7f, 0.7f),
                        new FPoint(0.8f, 0.7f),
                        new FPoint(0.8f, 0.8f),
                        new FPoint(0.9f, 0.8f),
                        new FPoint(0.9f, 0.9f),
                        new FPoint(1f, 1f)
                )));
    }


    private void initUI() {
        pattern = Pattern.compile(CommentListAdapter.URL_REGEX);
        gift_container = (LinearLayout) findViewById(R.id.gift_container);
        gift_price_container = (LinearLayout) findViewById(R.id.gift_price_container);
        mShapeFlyer = (ShapeFlyer) findViewById(R.id.floating_container);
        mShapeFlyer.setOnClickListener(this);
        imageUrl = Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL);
        frameLayoutCommentGift = (FrameLayout) findViewById(R.id.frameLayoutCommentGift);
        fm = getSupportFragmentManager();
        btnGift = (ImageView) findViewById(R.id.btnGift);
        btnGift.setOnClickListener(this);
        btnLike = (ImageView) findViewById(R.id.btnLike);
        btnLike.setOnClickListener(this);
        txtLikes = (TextView) findViewById(R.id.txtLikes);
        txtLikes.setOnClickListener(this);
        txtViews = (TextView) findViewById(R.id.txtViews);
        txtGiftsCount = (TextView) findViewById(R.id.txtGiftsCount);
        txtTaka = (TextView) findViewById(R.id.txtTaka);
        listOfComment = (ListView) findViewById(R.id.listComment);
        etComment = (EditText) findViewById(R.id.etComment);
        // used this method for showing edittext view when keyboard shows
//        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);

        btnSendComment = (Button) findViewById(R.id.btnSendComment);
        btnSendComment.setOnClickListener(this);

        LiveRoomActivity.this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // hide ic_like button for celebrity
        // celebruty can not give ic_like
        if (Session.isCeleb(getApplicationContext(), Session.IS_CELEB)) {
            btnLike.setVisibility(View.INVISIBLE);
            btnGift.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);

        Intent i = getIntent();
        int cRole = i.getIntExtra(ConstantApp.ACTION_KEY_CROLE, 0);

        if (cRole == 0) {
            throw new RuntimeException("Should not reach here");
        }

        roomName = i.getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
        Log.d("room_name", roomName);
        String user = i.getStringExtra("user");
        Log.d("fbName", user);

        if (user.equals("celeb")) {
            gift_container.setVisibility(View.VISIBLE);
            gift_price_container.setVisibility(View.VISIBLE);
            String fb_name = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
            msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
            Log.d("fbName", "celeb " + fb_name);
            user_name = fb_name;
        } else if (user.equals("fan")) {
            //user_name = Session.retreiveName(getApplicationContext(), Session.USER_NAME);
            user_name = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
            Log.d("fbName", "fan " + user_name);
        }


        Log.d("user_name", user_name);


        doConfigEngine(cRole);

        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.setItemEventHandler(new VideoViewEventListener() {
            @Override
            public void onItemDoubleClick(View v, Object item) {
                log.debug("onItemDoubleClick " + v + " " + item);

                if (mUidsList.size() < 2) {
                    return;
                }

                if (mViewType == VIEW_TYPE_DEFAULT)
                    switchToSmallVideoView(((VideoStatusData) item).mUid);
                else
                    switchToDefaultVideoView();
            }
        });

        ImageView button1 = (ImageView) findViewById(R.id.btn_1);
        ImageView button2 = (ImageView) findViewById(R.id.btn_2);
        ImageView button3 = (ImageView) findViewById(R.id.btn_3);

        if (isBroadcaster(cRole)) {
            SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, 0));
            surfaceV.setZOrderOnTop(true);
            surfaceV.setZOrderMediaOverlay(true);

            mUidsList.put(0, surfaceV); // get first surface view

            mGridVideoViewContainer.initViewContainer(getApplicationContext(), 0, mUidsList); // first is now full view
            worker().preview(true, surfaceV, 0);
            broadcasterUI(button1, button2, button3);
            /*here videoid is the room name created on firebase database
            @video_id is used for creating a channel on firebase database.this id will randomly change when user
            come on live*/
            video_id = String.valueOf(Methods.getSerialnumber(8));
            Log.d("whoi", "broadcaster");

            // first staep to create room on firebase for comment
            createRoomOnFirebase(video_id);

            // save celebrity name and video id to server
            saveLiveData(video_id, roomName);

        } else {
            getVid(roomName);

            audienceUI(button1, button2, button3);
            Log.d("whoi", "Audience");

            // roomName = "Audience";
        }

        worker().joinChannel(roomName, config().mUid);

        TextView textRoomName = (TextView) findViewById(R.id.room_name);
        textRoomName.setText(roomName);
    }

    private void createRoomOnFirebase(String video_id) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(video_id, "");
        root.updateChildren(map);

        getAllLikeCeleb(video_id + "ic_like");

        getAllViewCeleb(video_id + "view");

    }

    private void createRoomOnFirebaseForLike(String video_id) {

        likeRoomName = video_id + "ic_like";
        root_like = FirebaseDatabase.getInstance().getReference().getRoot().child(likeRoomName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(likeRoomName, "");
        root.updateChildren(map);


        getAllLike();

    }

    private void createRoomOnFirebaseForView(String video_id) {

        viewRoomName = video_id + "view";
        root_view = FirebaseDatabase.getInstance().getReference().getRoot().child(viewRoomName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(viewRoomName, "");
        root.updateChildren(map);

        postView(1);

        getAllView();
    }

    private void getAllView() {
        root_view = FirebaseDatabase.getInstance().getReference().child(viewRoomName);

        root_view.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtViews.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAllViewCeleb(String s) {
        root_view = FirebaseDatabase.getInstance().getReference().child(s);

        root_view.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtViews.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveLiveData(String vid, String celeb_name) {

        String url = "http://vumobile.biz/Toukir/celeb_comment/saveroomname.php?vid=" + vid + "&celeb_name=" + celeb_name;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("TAGG", "GET response: " + response);
                getAllComment();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("TAGG", "Volley GET error: " + error);
            }
        });

        requestQueue.add(getRequest);

    }

    private void broadcasterUI(ImageView button1, ImageView button2, ImageView button3) {
        button1.setTag(true);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && (boolean) tag) {
                    doSwitchToBroadcaster(false);
                } else {
                    doSwitchToBroadcaster(true);
                }
            }
        });
        button1.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worker().getRtcEngine().switchCamera();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                boolean flag = true;
                if (tag != null && (boolean) tag) {
                    flag = false;
                }
                worker().getRtcEngine().muteLocalAudioStream(flag);
                ImageView button = (ImageView) v;
                button.setTag(flag);
                if (flag) {
                    button.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
                } else {
                    button.clearColorFilter();
                }
            }
        });
    }

    private void audienceUI(ImageView button1, ImageView button2, ImageView button3) {
        button1.setTag(null);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && (boolean) tag) {
                    doSwitchToBroadcaster(false);
                } else {
                    doSwitchToBroadcaster(true);
                }
            }
        });
        button1.setVisibility(View.GONE);
        //button1.clearColorFilter();
        button2.setVisibility(View.GONE);
        button3.setTag(null);
        button3.setVisibility(View.GONE);
        button3.clearColorFilter();
    }

    private void doConfigEngine(int cRole) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int prefIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (prefIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            prefIndex = ConstantApp.DEFAULT_PROFILE_IDX;
        }

        //int status = new NetworkState(getApplicationContext()).haveNetworkConnection();

        // if connected wifi
        if (1 == 1) {
            Log.d("Connected:", linkRate);
            int vProfile = Constants.VIDEO_PROFILE_180P_4;
            // default smothness 1.0f and lightness 0.65f
            // if we want set manually smothness and lightness then uncomment below method and set smothness and lightness
            worker().setPreParameters(0.50f, 0.50f);
            worker().configEngine(cRole, vProfile);

//            Log.d("Connected:","wifi");
//            if (signal.matches("good")){
//                Log.d("signal:","good");
//                // previous code
//                //int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];
//                // my code
//                int vProfile = Constants.VIDEO_PROFILE_240P;
//                // default smothness 1.0f and lightness 0.65f
//                // if we want set manually smothness and lightness then uncomment below method and set smothness and lightness
//                worker().setPreParameters(.50f,1.0f);
//                worker().configEngine(cRole, vProfile);
//            }else if ( signal.matches("fair")){
//                Log.d("signal:","fair");
//                // previous code
//                //int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];
//                // my code
//                int vProfile = Constants.VIDEO_PROFILE_360P_4;
//                // default smothness 1.0f and lightness 0.65f
//                // if we want set manually smothness and lightness then uncomment below method and set smothness and lightness
//                worker().setPreParameters(2.0f,1.0f);
//                worker().configEngine(cRole, vProfile);
//
//            }else if ( signal.matches("poor")){
//                Log.d("signal:","poor");
//                // previous code
//                //int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];
//                // my code
//                int vProfile = Constants.VIDEO_PROFILE_180P;
//                // default smothness 1.0f and lightness 0.65f
//                // if we want set manually smothness and lightness then uncomment below method and set smothness and lightness
//                worker().setPreParameters(.50f,1.0f);
//                worker().configEngine(cRole, vProfile);
//
//            }else if (signal.matches("excellent")){
//                Log.d("signal:","excellent");
//                // previous code
//                //int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];
//                // my code
//                int vProfile = Constants.VIDEO_PROFILE_720P;
//                // default smothness 1.0f and lightness 0.65f
//                // if we want set manually smothness and lightness then uncomment below method and set smothness and lightness
//                worker().setPreParameters(3.0f,1.0f);
//                worker().configEngine(cRole, vProfile);


            // if conneted apn
        } else if (0 == 0)

        {
            Log.d("Connected:", "apn");
            // previous code
            //int vProfile = ConstantApp.VIDEO_PROFILES[prefIndex];
            // my code
            int vProfile = Constants.VIDEO_PROFILE_180P_4;
            // default smothness 1.0f and lightness 0.65f
            // if we want set manually smothness and lightness then uncomment below method and set smothness and lightness
            worker().setPreParameters(0.5f, 0.50f);
            worker().configEngine(cRole, vProfile);
        }

    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);

        mUidsList.clear();
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            worker().preview(false, null, 0);
        }
    }

    public void onClickClose(View view) {
        new ServerPostRequest().onLive(getApplicationContext(), msisdn, "0");
        finish();
    }

    public void onShowHideClicked(View view) {
        boolean toHide = true;
        if (view.getTag() != null && (boolean) view.getTag()) {
            toHide = false;
        }
        view.setTag(toHide);

        doShowButtons(toHide);
    }

    private void doShowButtons(boolean hide) {
        View topArea = findViewById(R.id.top_area);
        topArea.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);

        View button1 = findViewById(R.id.btn_1);
        button1.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);

        View button2 = findViewById(R.id.btn_2);
        View button3 = findViewById(R.id.btn_3);
        if (isBroadcaster()) {
            button2.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
            button3.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        } else {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }

    private void doSwitchToBroadcaster(boolean broadcaster) {
        final int currentHostCount = mUidsList.size();
        final int uid = config().mUid;
        log.debug("doSwitchToBroadcaster " + currentHostCount + " " + (uid & 0XFFFFFFFFL) + " " + broadcaster);

        if (broadcaster) {
            doConfigEngine(Constants.CLIENT_ROLE_BROADCASTER);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doRenderRemoteUi(uid);

                    ImageView button1 = (ImageView) findViewById(R.id.btn_1);
                    ImageView button2 = (ImageView) findViewById(R.id.btn_2);
                    ImageView button3 = (ImageView) findViewById(R.id.btn_3);
                    broadcasterUI(button1, button2, button3);

                    doShowButtons(false);
                }
            }, 1000); // wait for reconfig engine
        } else {
            stopInteraction(currentHostCount, uid);
        }
    }

    private void stopInteraction(final int currentHostCount, final int uid) {
        doConfigEngine(Constants.CLIENT_ROLE_AUDIENCE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doRemoveRemoteUi(uid);

                ImageView button1 = (ImageView) findViewById(R.id.btn_1);
                ImageView button2 = (ImageView) findViewById(R.id.btn_2);
                ImageView button3 = (ImageView) findViewById(R.id.btn_3);
                audienceUI(button1, button2, button3);

                doShowButtons(false);
            }
        }, 1000); // wait for reconfig engine
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);
                mUidsList.put(uid, surfaceV);
                if (config().mUid == uid) {
                    rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                } else {
                    rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                }

                if (mViewType == VIEW_TYPE_DEFAULT) {
                    log.debug("doRenderRemoteUi VIEW_TYPE_DEFAULT" + " " + (uid & 0xFFFFFFFFL));
                    switchToDefaultVideoView();
                } else {
                    int bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                    log.debug("doRenderRemoteUi VIEW_TYPE_SMALL" + " " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    log.debug("already added to UI, ignore it " + (uid & 0xFFFFFFFFL) + " " + mUidsList.get(uid));
                    return;
                }

                final boolean isBroadcaster = isBroadcaster();
                log.debug("onJoinChannelSuccess " + channel + " " + uid + " " + elapsed + " " + isBroadcaster);

                worker().getEngineConfig().mUid = uid;

                SurfaceView surfaceV = mUidsList.remove(0);
                if (surfaceV != null) {
                    mUidsList.put(uid, surfaceV);
                }
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        log.debug("onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
        //showOffLineAlert(uid);
        doRemoveRemoteUi(uid);
    }

    private void showOffLineAlert(int uID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Celebrity goes offline!");
        alertDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new ServerPostRequest().onLive(getApplicationContext(), msisdn, "0");

                finish();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void requestRemoteStreamType(final int currentHostCount) {
        log.debug("requestRemoteStreamType " + currentHostCount);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap.Entry<Integer, SurfaceView> highest = null;
                for (HashMap.Entry<Integer, SurfaceView> pair : mUidsList.entrySet()) {
                    log.debug("requestRemoteStreamType " + currentHostCount + " local " + (config().mUid & 0xFFFFFFFFL) + " " + (pair.getKey() & 0xFFFFFFFFL) + " " + pair.getValue().getHeight() + " " + pair.getValue().getWidth());
                    if (pair.getKey() != config().mUid && (highest == null || highest.getValue().getHeight() < pair.getValue().getHeight())) {
                        if (highest != null) {
                            rtcEngine().setRemoteVideoStreamType(highest.getKey(), Constants.VIDEO_STREAM_LOW);
                            log.debug("setRemoteVideoStreamType switch highest VIDEO_STREAM_LOW " + currentHostCount + " " + (highest.getKey() & 0xFFFFFFFFL) + " " + highest.getValue().getWidth() + " " + highest.getValue().getHeight());
                        }
                        highest = pair;
                    } else if (pair.getKey() != config().mUid && (highest != null && highest.getValue().getHeight() >= pair.getValue().getHeight())) {
                        rtcEngine().setRemoteVideoStreamType(pair.getKey(), Constants.VIDEO_STREAM_LOW);
                        log.debug("setRemoteVideoStreamType VIDEO_STREAM_LOW " + currentHostCount + " " + (pair.getKey() & 0xFFFFFFFFL) + " " + pair.getValue().getWidth() + " " + pair.getValue().getHeight());
                    }
                }
                if (highest != null && highest.getKey() != 0) {
                    rtcEngine().setRemoteVideoStreamType(highest.getKey(), Constants.VIDEO_STREAM_HIGH);
                    log.debug("setRemoteVideoStreamType VIDEO_STREAM_HIGH " + currentHostCount + " " + (highest.getKey() & 0xFFFFFFFFL) + " " + highest.getValue().getWidth() + " " + highest.getValue().getHeight());
                }
            }
        }, 500);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                mUidsList.remove(uid);

                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }

                log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));

                if (mViewType == VIEW_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null)
            mSmallVideoViewDock.setVisibility(View.GONE);
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), config().mUid, mUidsList);

        mViewType = VIEW_TYPE_DEFAULT;

        int sizeLimit = mUidsList.size();
        if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
        }
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                rtcEngine().setRemoteVideoStreamType(uid, Constants.VIDEO_STREAM_HIGH);
                log.debug("setRemoteVideoStreamType VIDEO_STREAM_HIGH " + mUidsList.size() + " " + (uid & 0xFFFFFFFFL));
            }
        }
    }

    private void switchToSmallVideoView(int uid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(uid, mUidsList.get(uid));
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), uid, slice);

        bindToSmallVideoView(uid);

        mViewType = VIEW_TYPE_SMALL;

        requestRemoteStreamType(mUidsList.size());
    }

    public int mViewType = VIEW_TYPE_DEFAULT;

    public static final int VIEW_TYPE_DEFAULT = 0;

    public static final int VIEW_TYPE_SMALL = 1;

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, exceptUid, mUidsList, new VideoViewEventListener() {
                @Override
                public void onItemDoubleClick(View v, Object item) {
                    switchToDefaultVideoView();
                }
            });
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);

        recycler.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(mSmallVideoViewAdapter);

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSendComment:
                String comment = etComment.getText().toString();
                // add comment to the comment list here
                postComment(getApplicationContext(), comment,"0");
                // hide keyboard
                hideKeyboard();
                //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                etComment.setText("");
                break;
            case R.id.btnLike:
                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
                mShapeFlyer.startAnimation(R.drawable.ic_like);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                btnLike.startAnimation(myAnim);
//                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
//                btnLike.startAnimation(myAnim);
                i++;
                postLike(1);
                getAllLike();

                break;

            case R.id.btnGift:
                // TODO
                ft = fm.beginTransaction();
                Log.d("ftft 1 ", "onClick: " + fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount() == 0) {

                    // add
                    ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
                    ft.add(R.id.frameLayoutCommentGift, new Gifts());
                    ft.addToBackStack("ttt");
                    ft.commit();
                    Log.d("ftft 2 ", "onClick: " + fm.getBackStackEntryCount());
                }

                break;
            default:
                break;
        }

    }

    public static void postComment(Context context, String comment, String priceOfGift) {

        Map<String, Object> map = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference message_root = root.child(temp_key);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", user_name);
        map2.put("msg", comment);
        map2.put("imageUrl", imageUrl);
        map2.put("msisdn",Session.retreivePhone(context,Session.USER_PHONE));
        map2.put("gift_price",priceOfGift);

        message_root.updateChildren(map2);

//        listOfComment.setSelection(adapter.getCount() - 1);

    }

    public static void postLike(int i) {

        Map<String, Object> map = new HashMap<String, Object>();
        temp_key = root_like.push().getKey();
        root_like.updateChildren(map);

        DatabaseReference message_root = root_like.child(temp_key);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", user_name);
        map3.put("count", i);
        message_root.updateChildren(map3);

//        listOfComment.setSelection(adapter.getCount() - 1);

    }

    public static void postView(int i) {

        Map<String, Object> map = new HashMap<String, Object>();
        temp_key = root_view.push().getKey();
        root_view.updateChildren(map);

        DatabaseReference message_root = root_view.child(temp_key);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("name", user_name);
        map4.put("count", i);
        message_root.updateChildren(map4);

//        listOfComment.setSelection(adapter.getCount() - 1);

    }


    public String getTime() {
        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }

    public void getAllLike() {
        root_like = FirebaseDatabase.getInstance().getReference().child(likeRoomName);

        root_like.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                append_like(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllLikeCeleb(String roomNam) {
        root_like = FirebaseDatabase.getInstance().getReference().child(roomNam);
        root_like.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                append_like(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // this method will b removed..this method is used for only celebrity
    public void getAllComment() {
        root = FirebaseDatabase.getInstance().getReference().child(video_id);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // this method is used for only audience
    public void getAllComment(String test) {
        root = FirebaseDatabase.getInstance().getReference().child(test);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String chat_msg, imageUrlProfile, price;

     int g_price = 0;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            price = (String) ((DataSnapshot) i.next()).getValue();
            imageUrlProfile = (String) ((DataSnapshot) i.next()).getValue();
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            comment_msisdn = (String) ((DataSnapshot) i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot) i.next()).getValue();

            int p = Integer.parseInt(price);

            m = pattern.matcher(chat_msg);

            if (m.find()){
                COUNT_GIFTS++;
                txtGiftsCount.setText(String.valueOf(COUNT_GIFTS));
                g_price = g_price + p;
                txtTaka.setText(String.valueOf(g_price));
            }

            CommentClass commentClass = new CommentClass();
            commentClass.setUserName(chat_user_name);
            commentClass.setuComment(chat_msg);
            commentClass.setImage(imageUrlProfile);

            commentClassList.add(commentClass);
        }
        listOfComment.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void append_like(DataSnapshot dataSnapshot) {

        if (dataSnapshot.getChildrenCount() > 0) {
            mShapeFlyer.startAnimation(R.drawable.ic_like);
        }

        txtLikes.setText(String.valueOf(dataSnapshot.getChildrenCount()));
        Log.d("countttttttttttttttttttttt", String.valueOf(dataSnapshot.getChildrenCount()));
    }

    // get random id which is the channel name on firebase
    public void getVid(String name) {
        String url = "http://vumobile.biz/Toukir/celeb_comment/getVid.php?room_name=" + name;
        RequestQueue queue = Volley.newRequestQueue(LiveRoomActivity.this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("log", response.toString());

                        try {
                            JSONArray array = response.getJSONArray("server_response");
                            JSONObject object = array.getJSONObject(0);
                            id = object.getString("vid");
                            createRoomOnFirebaseForLike(id);
                            createRoomOnFirebaseForView(id);
                            Log.d("logs", id + " " + op);
                            op = id;

                            getAllComment(id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsObjRequest);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d("ftft 00", "onBackPressed: " + fm.getBackStackEntryCount());
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
            Log.d("ftft 66", "onBackPressed: ");
        } else {
            showAlert();
            Log.d("ftft 77", "onBackPressed: ");
        }


    }

    private void showAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Are you sure you want to go offline?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new ServerPostRequest().onLive(getApplicationContext(), msisdn, "0");
                finish();

            }
        });

        alertDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick
                    (DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}