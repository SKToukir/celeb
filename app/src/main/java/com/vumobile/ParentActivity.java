package com.vumobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.CelebrityListAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.CelebrityClass;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.ui.BaseActivity;
import com.vumobile.celeb.ui.LiveRoomActivity;
import com.vumobile.celeb.ui.MessageActivity;
import com.vumobile.fan.login.FanCelebProfileActivity;
import com.vumobile.fan.login.LogInAcitvity;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.serverrequest.AllVolleyInterfaces;
import com.vumobile.fan.login.serverrequest.MyVolleyRequest;
import com.vumobile.fan.login.ui.FanNotificationActivity;
import com.vumobile.fan.login.ui.fragment.Credits;
import com.vumobile.fan.login.ui.fragment.History;
import com.vumobile.fan.login.ui.fragment.MyGallery;
import com.vumobile.fan.login.ui.fragment.Transaction;
import com.vumobile.notification.MyReceiver;
import com.vumobile.notification.NetworkedService;
import com.vumobile.service.MyFirebaseInstanceIDService;
import com.vumobile.utils.MyInternetCheckReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.agora.rtc.Constants;

public class ParentActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private CelebrityClass celebrityClass;
    private List<CelebrityClass> celebrityClassList = new ArrayList<CelebrityClass>();
    private List<CelebrityClass> celebrityClassListCopy;
    private CelebrityListAdapter adapter;
    private ListView listCeleb;
    private PendingIntent pendingIntent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private SwipeRefreshLayout swipeRefreshLayout;

    Button buttonFilterAll, buttonFilterFollowing, buttonFilterLive;
    Toolbar toolbar;
    ImageView imageViewNotification, imageViewMessage;
    TextView navUserName;
    ImageView navUserPic;
    // drawer menu
    ImageView imageViewHome, imageViewMyGallery, imageViewHistory, imageViewTransaction, imageViewCredits, imageViewLogout;
    static RelativeLayout content_parent;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    LinearLayout linearLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        startService(new Intent(ParentActivity.this, MyFirebaseInstanceIDService.class));
        notificationRegister();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();

        // Initialize comment list adapter
        adapter = new CelebrityListAdapter(this, R.layout.celeb_list_row, celebrityClassList);
        listCeleb.setAdapter(adapter);

        Log.d("Session: ", Session.retreiveName(ParentActivity.this, Session.USER_NAME));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadFanProfileData(Api.URL_GET_CELEB_PROFILE);
        View hView = navigationView.getHeaderView(0);
        navUserName = (TextView) hView.findViewById(R.id.textView);
        navUserPic = (ImageView) hView.findViewById(R.id.imageView);

        //  loadCelebrityData(Api.URL_ACTIVATE_USERS);

        listCeleb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String name = celebrityClassList.get(i).getCeleb_name();
                String msisdn = celebrityClassList.get(i).getCeleb_code();
                String fbName = celebrityClassList.get(i).getFb_name();
                String profilePic = celebrityClassList.get(i).getCeleb_image();
                String isOnline = celebrityClassList.get(i).getIsOnline();
                String followerCount = celebrityClassList.get(i).getFollowerCount();

                if (isOnline.equals("1") || isOnline.matches("1")) {
                    String room = fbName;
                    Intent in = new Intent(ParentActivity.this, LiveRoomActivity.class);
                    in.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                    in.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room);
                    in.putExtra("user", "fan");
                    startActivity(in);
                } else {
                    Intent intent = new Intent(ParentActivity.this, FanCelebProfileActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("msisdn", msisdn);
                    intent.putExtra("fbname", fbName);
                    intent.putExtra("profilePic", profilePic);
                    intent.putExtra("FOLLOWER", followerCount);
                    startActivity(intent);
                }

            }
        });

        try {

            Intent serviceIntent = new Intent(ParentActivity.this, NetworkedService.class);
            startService(serviceIntent);
            Intent myIntent = new Intent(ParentActivity.this, MyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(ParentActivity.this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60 * 1000, pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // show snackbar while no internet
        MyInternetCheckReceiver.isNetworkAvailableShowSnackbar(this, linearLayoutMain);
        new MyInternetCheckReceiver(linearLayoutMain);


    } // end of onCreate

    // retrieve device token for push notification
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

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();

    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Api.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        storeRegId(regId);

        Log.e("TAG", "Firebase reg id: " + regId);
        Log.e("taggg", "Firebase:" + regId);
    }

    private void storeRegId(String regId) {

        HashMap<String, String> params = new HashMap<>();
        params.put("MSISDN", Session.retreivePhone(getApplicationContext(), Session.USER_PHONE));
        params.put("RegId", regId);

        MyVolleyRequest.setRegId(getApplicationContext(), Request.Method.POST, Api.URL_SET_REG_ID, params, new AllVolleyInterfaces.ResponseString() {
            @Override
            public void getResponse(String responseResult) {
                Log.d("FromServerrrrrr", " " + responseResult + "");
            }

            @Override
            public void getResponseErr(String responseResultErr) {
                Log.d("FromServer", responseResultErr + "");
            }
        });
    }

    private void loadFanProfileData(String urlFanProfile) {

        String fullUrl = urlFanProfile + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        Log.d("fanurl", "loadFanProfileData: " + fullUrl);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer fan p", jsonObject.toString());
                try {
                    JSONArray array = jsonObject.getJSONArray("result");
                    JSONObject obj = array.getJSONObject(0);
                    navUserName.setText(obj.getString("Name"));
                    Glide.with(ParentActivity.this).load(obj.getString("Image_url")).into(navUserPic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer fan p", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
            }
        });

        //Adding request to the queue
        Volley.newRequestQueue(ParentActivity.this).add(request);

    }

    private void loadCelebrityData(String urlCelebrity) {
        swipeRefreshLayout.setRefreshing(true);
        celebrityClassList.clear();

        String fullUrl = urlCelebrity + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer", jsonObject.toString());

                try {
                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        celebrityClass = new CelebrityClass();
                        celebrityClass.setCeleb_name(obj.getString(Api.CELEB_NAME));
                        celebrityClass.setCeleb_code(obj.getString(Api.CELEB_CODE_MSISDN));
                        celebrityClass.setCeleb_image(obj.getString(Api.CELEB_IMAGE));
                        celebrityClass.setFb_name(obj.getString("Name"));
                        celebrityClass.setIsOnline(obj.getString("Live_status"));
                        celebrityClass.setIsfollow(obj.getString("Isfollow"));
                        celebrityClass.setFollowerCount(obj.getString("Follower"));

                        celebrityClassList.add(celebrityClass);


                        listCeleb.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    celebrityClassListCopy = new ArrayList<>();
                    celebrityClassListCopy.addAll(celebrityClassList);
                    Log.d("hellot", "onResponse: " + celebrityClassList.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ParentActivity.this);

        //Adding request to the queue
        requestQueue.add(request);

    }

    private void loadCelebrityDataWhoIsLive(String urlCelebrity) {
        swipeRefreshLayout.setRefreshing(true);
        celebrityClassList.clear();
        String fullUrl = urlCelebrity + "&MSISDN=" + Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer", jsonObject.toString());

                try {
                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        celebrityClass = new CelebrityClass();
                        if (obj.getString("Live_status").equals("1")) {
                            celebrityClass.setCeleb_name(obj.getString(Api.CELEB_NAME));
                            celebrityClass.setCeleb_code(obj.getString(Api.CELEB_CODE_MSISDN));
                            celebrityClass.setCeleb_image(obj.getString(Api.CELEB_IMAGE));
                            celebrityClass.setFb_name(obj.getString("Name"));
                            celebrityClass.setIsOnline(obj.getString("Live_status"));
                            celebrityClass.setIsfollow(obj.getString("Isfollow"));
                            celebrityClass.setFollowerCount(obj.getString("Follower"));

                            celebrityClassList.add(celebrityClass);
                        }

                        listCeleb.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                    celebrityClassListCopy = new ArrayList<>();
                    celebrityClassListCopy.addAll(celebrityClassList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ParentActivity.this);

        //Adding request to the queue
        requestQueue.add(request);

    }

    private void loadCelebrityDataWhoIsFollowing(String urlCelebrityFollowing, String mFanPhone) {

        swipeRefreshLayout.setRefreshing(true);
        celebrityClassList.clear();
        String fullUrl = urlCelebrityFollowing + "&MSISDN=" + mFanPhone;
        Log.d("Full url", "loadCelebrityDataWhoIsFollowing: " + fullUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("FromServer 13", jsonObject.toString());

                try {
                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i <= array.length() - 1; i++) {

                        JSONObject obj = array.getJSONObject(i);
                        celebrityClass = new CelebrityClass();

                        celebrityClass.setCeleb_name(obj.getString(Api.CELEB_NAME));
                        celebrityClass.setCeleb_code(obj.getString(Api.CELEB_CODE_MSISDN));
                        celebrityClass.setCeleb_image(obj.getString(Api.CELEB_IMAGE));
                        celebrityClass.setFb_name(obj.getString("Name"));
                        celebrityClass.setIsOnline(obj.getString("Live_status"));
                        celebrityClass.setIsfollow("1");
                        celebrityClass.setFollowerCount(obj.getString("Follower"));

                        celebrityClassList.add(celebrityClass);

                        listCeleb.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    celebrityClassListCopy = new ArrayList<>();
                    celebrityClassListCopy.addAll(celebrityClassList);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("FromServer", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ParentActivity.this);

        //Adding request to the queue
        requestQueue.add(request);

    }

    private void initUI() {

        // drawer menu
        content_parent = (RelativeLayout) findViewById(R.id.content_parent);

        imageViewHome = (ImageView) findViewById(R.id.imageViewHome);
        imageViewMyGallery = (ImageView) findViewById(R.id.imageViewMyGallery);
        imageViewHistory = (ImageView) findViewById(R.id.imageViewHistory);
        imageViewTransaction = (ImageView) findViewById(R.id.imageViewTransaction);
        imageViewCredits = (ImageView) findViewById(R.id.imageViewCredits);
        imageViewLogout = (ImageView) findViewById(R.id.imageViewLogout);

        imageViewHome.setOnClickListener(this);
        imageViewMyGallery.setOnClickListener(this);
        imageViewHistory.setOnClickListener(this);
        imageViewTransaction.setOnClickListener(this);
        imageViewCredits.setOnClickListener(this);
        imageViewLogout.setOnClickListener(this);


        listCeleb = (ListView) findViewById(R.id.list_of_celeb);

        imageViewNotification = (ImageView) toolbar.findViewById(R.id.imageViewNotification);
        imageViewMessage = (ImageView) toolbar.findViewById(R.id.imageViewMessage);
        imageViewNotification.setOnClickListener(this);
        imageViewMessage.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        buttonFilterAll = (Button) findViewById(R.id.buttonFilterAll);
        buttonFilterFollowing = (Button) findViewById(R.id.buttonFilterFollowing);
        buttonFilterLive = (Button) findViewById(R.id.buttonFilterLive);
        buttonFilterAll.setOnClickListener(this);
        buttonFilterFollowing.setOnClickListener(this);
        buttonFilterLive.setOnClickListener(this);
        buttonFilterAll.setTag("SELECT_ITEM");
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        //celebrityClassList = new ArrayList<CelebrityClass>();
                                        if (buttonFilterAll.getTag().equals("SELECT_ITEM")) {
                                            loadCelebrityData(Api.URL_ACTIVATE_USERS);
                                        } else if (buttonFilterFollowing.getTag().equals("SELECT_ITEM")) {
                                            loadCelebrityDataWhoIsFollowing(Api.URL_GET_FOLLOW_CELEB_LIST, Session.retreivePhone(ParentActivity.this, Session.USER_PHONE));
                                        } else if (buttonFilterLive.getTag().equals("SELECT_ITEM")) {
                                            loadCelebrityDataWhoIsLive(Api.URL_ACTIVATE_USERS);
                                        }

                                    }
                                }
        );

        linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayoutMain);


    }

    @Override
    public void onRefresh() {
        if (buttonFilterAll.getTag().equals("SELECT_ITEM")) {
            loadCelebrityData(Api.URL_ACTIVATE_USERS);
        } else if (buttonFilterFollowing.getTag().equals("SELECT_ITEM")) {
            loadCelebrityDataWhoIsFollowing(Api.URL_GET_FOLLOW_CELEB_LIST, Session.retreivePhone(ParentActivity.this, Session.USER_PHONE));
        } else if (buttonFilterLive.getTag().equals("SELECT_ITEM")) {
            loadCelebrityDataWhoIsLive(Api.URL_ACTIVATE_USERS);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    SearchView searchView = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //    getMenuInflater().inflate(R.menu.parent, menu);

        getMenuInflater().inflate(R.menu.parent, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                // Toast.makeText(ParentActivity.this, "" + query, Toast.LENGTH_SHORT).show();

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                Toast.makeText(ParentActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                // Filter
                s = s.toLowerCase(Locale.getDefault());
                celebrityClassList.clear();
                if (s.length() == 0) {
                    celebrityClassList.addAll(celebrityClassListCopy);
                } else {
                    for (int i = 0; i < celebrityClassListCopy.size(); i++) {
                        if (celebrityClassListCopy.get(i).getCeleb_name().toLowerCase(Locale.getDefault()).contains(s)) {
                            celebrityClassList.add(celebrityClassListCopy.get(i));
                        }
                    }
                    Log.d("foreach", "onQueryTextChange: " + celebrityClassListCopy.size());
                }

                adapter.notifyDataSetChanged();

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //  Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            return true;
        }

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

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_transaction) {

        } else if (id == R.id.nav_credits) {

        } else if (id == R.id.nav_logout) {
            Session.clearAllSharedData(getApplicationContext());
            Intent intent = new Intent(ParentActivity.this, LogInAcitvity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageViewNotification:
                startActivity(new Intent(getApplicationContext(), FanNotificationActivity.class));
                break;

            case R.id.imageViewMessage:
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonFilterAll:
                loadCelebrityData(Api.URL_ACTIVATE_USERS);
                changeButtonSelectFocus(buttonFilterAll);
                break;

            case R.id.buttonFilterFollowing:
                loadCelebrityDataWhoIsFollowing(Api.URL_GET_FOLLOW_CELEB_LIST, Session.retreivePhone(ParentActivity.this, Session.USER_PHONE));
                changeButtonSelectFocus(buttonFilterFollowing);
                break;

            case R.id.buttonFilterLive:
                loadCelebrityDataWhoIsLive(Api.URL_ACTIVATE_USERS);
                changeButtonSelectFocus(buttonFilterLive);
                break;

            // drawer menu items
            // imageViewHome, imageViewMyGallery, imageViewHistory, imageViewTransaction, imageViewCredits
            case R.id.imageViewHome:
                FragmentManager fm = getSupportFragmentManager();
                int count = fm.getBackStackEntryCount();
                for (int i = 0; i < count; ++i) {
                    fm.popBackStackImmediate();
                }

                drawer.closeDrawers();
                break;

            case R.id.imageViewMyGallery:
                // Create new fragment and transaction
                Fragment newFragment = new MyGallery();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.fragment_container_main, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

                drawer.closeDrawers();
                break;

            case R.id.imageViewHistory:
                // Create new fragment and transaction
                Fragment fragmentHistory = new History();
                FragmentTransaction transactionHistory = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transactionHistory.replace(R.id.fragment_container_main, fragmentHistory);
                transactionHistory.addToBackStack(null);

                // Commit the transaction
                transactionHistory.commit();


                drawer.closeDrawers();
                break;

            case R.id.imageViewTransaction:
                // Create new fragment and transaction
                Fragment fragmentTransaction = new Transaction();
                FragmentTransaction transactionTrans = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transactionTrans.replace(R.id.fragment_container_main, fragmentTransaction);
                transactionTrans.addToBackStack(null);

                // Commit the transaction
                transactionTrans.commit();


                drawer.closeDrawers();
                break;

            case R.id.imageViewCredits:
                // Create new fragment and transaction
                Fragment fragmentCredit = new Credits();
                FragmentTransaction transactionCre = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transactionCre.replace(R.id.fragment_container_main, fragmentCredit);
                transactionCre.addToBackStack(null);

                // Commit the transaction
                transactionCre.commit();

                drawer.closeDrawers();
                break;

            case R.id.imageViewLogout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParentActivity.this);
                alertDialog.setTitle("Logout Alert");
                alertDialog.setMessage("Want to logout?");
                alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_action_alert));
                alertDialog.setPositiveButton("YES", (dialog, which) -> {
                    Session.clearAllSharedData(getApplicationContext());
                    Intent inte = new Intent(ParentActivity.this, LogInAcitvity.class);
                    inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(inte);
                    this.finish();
                    finish();
                });
                alertDialog.setNegativeButton("NO", (dialog, which) -> {

                });

                alertDialog.create().show();

                drawer.closeDrawers();
                break;
        }

    }

    private void changeButtonSelectFocus(Button button) {
        buttonFilterAll.setBackgroundColor(getResources().getColor(R.color.white));
        buttonFilterAll.setTextColor(getResources().getColor(R.color.myColorTwoHeader));
        buttonFilterAll.setTag("ITEM");
        buttonFilterFollowing.setBackgroundColor(getResources().getColor(R.color.white));
        buttonFilterFollowing.setTextColor(getResources().getColor(R.color.myColorTwoHeader));
        buttonFilterFollowing.setTag("ITEM");
        buttonFilterLive.setBackgroundColor(getResources().getColor(R.color.white));
        buttonFilterLive.setTextColor(getResources().getColor(R.color.myColorTwoHeader));
        buttonFilterLive.setTag("ITEM");

        button.setBackgroundColor(getResources().getColor(R.color.myColorTwoHeader));
        button.setTextColor(getResources().getColor(R.color.white));
        button.setTag("SELECT_ITEM");
    }

    @Override
    protected void onResume() {
        // show snackbar while no internet
        MyInternetCheckReceiver.isNetworkAvailableShowSnackbar(this, linearLayoutMain);
        super.onResume();
    }
}
