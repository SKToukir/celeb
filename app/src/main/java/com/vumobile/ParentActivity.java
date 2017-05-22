package com.vumobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.CelebrityListAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.Utils.CelebrityClass;
import com.vumobile.celeb.model.ConstantApp;
import com.vumobile.celeb.ui.BaseActivity;
import com.vumobile.celeb.ui.LiveRoomActivity;
import com.vumobile.celeb.ui.MessageActivity;
import com.vumobile.fan.login.FanCelebProfileActivity;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.ui.FanNotificationActivity;
import com.vumobile.notification.MyReceiver;
import com.vumobile.notification.NetworkedService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.Constants;

public class ParentActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private CelebrityClass celebrityClass;
    private List<CelebrityClass> celebrityClassList = new ArrayList<CelebrityClass>();
    private CelebrityListAdapter adapter;
    private ListView listCeleb;
    private PendingIntent pendingIntent;

    private SwipeRefreshLayout swipeRefreshLayout;

    Button buttonFilterAll, buttonFilterFollowing, buttonFilterLive;
    Toolbar toolbar;
    ImageView imageViewNotification, imageViewMessage;
    TextView navUserName;
    ImageView navUserPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        displayFirebaseRegId();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();

        // initialize comment list adapter
        adapter = new CelebrityListAdapter(this, R.layout.celeb_list_row, celebrityClassList);
        listCeleb.setAdapter(adapter);

        Log.d("Session: ", Session.retreiveName(ParentActivity.this, Session.USER_NAME));
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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

        Log.e("TAG", "Firebase reg id: " + regId);
        Log.e("taggg", "Firebase:" + regId);
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

                        celebrityClassList.add(celebrityClass);

                        listCeleb.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

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

                            celebrityClassList.add(celebrityClass);
                        }

                        listCeleb.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

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
                Log.d("FromServer 12", jsonObject.toString());

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

                        celebrityClassList.add(celebrityClass);

                        listCeleb.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

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
            //    Toast.makeText(ParentActivity.this, "" + query, Toast.LENGTH_SHORT).show();

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(ParentActivity.this, "" + s, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
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


//            case R.id.btnGoLive:
//                startActivity(new Intent(ParentActivity.this, MainActivityLive.class));
//                break;
//            case R.id.btnFanViewLive:
//                startActivity(new Intent(ParentActivity.this, MainActivityLive.class));
//                break;
//            default:
//                break;
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


}
