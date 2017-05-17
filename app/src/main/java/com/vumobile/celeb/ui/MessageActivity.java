package com.vumobile.celeb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.MessageUserListAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.MessageListClass;
import com.vumobile.fan.login.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private MessageUserListAdapter adapter;
    private List<MessageListClass> listClasses = new ArrayList<MessageListClass>();
    private MessageListClass requestClass;
    private ListView listView;
    private Toolbar toolbar;
    private ImageView imgBack;
    private Intent intent;
    private String profilePic, fbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);

        initUI();

        retreiveData(Api.URL_GET_SCHEDULES);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageListClass messageListClass = listClasses.get(i);
                String name = messageListClass.getName();
                Log.d("FromServer", name);
                String chat_room_name = messageListClass.getRoom_number();
                Log.d("FromServer", chat_room_name);

                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                intent.putExtra("room",chat_room_name);
                intent.putExtra("imageUrl",profilePic);
                intent.putExtra("name",fbName);
                startActivity(intent);
            }
        });

    }

    private void retreiveData(String urlGetSchedules) {

        String msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetSchedules,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray array = object.getJSONArray("result");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject obj = array.getJSONObject(i);
                                requestClass = new MessageListClass();

                                requestClass.setName(obj.getString("Name"));
                                Log.d("FromServer", requestClass.getName());
                                requestClass.setImageUrl(obj.getString("Image_url"));
                                Log.d("FromServer", requestClass.getImageUrl());
                                requestClass.setRoom_number(obj.getString("RoomNumber"));
                                Log.d("FromServer", requestClass.getRoom_number());

                                listClasses.add(requestClass);

                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

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
                params.put("flag", "1");
                params.put("MSISDN", msisdn);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initUI() {

        listView = (ListView) findViewById(R.id.listChatUser);
        imgBack = (ImageView) toolbar.findViewById(R.id.backCelebMessage);
        imgBack.setOnClickListener(this);

        adapter = new MessageUserListAdapter(getApplicationContext(), R.layout.row_message_fan_list, listClasses);
        listView.setAdapter(adapter);

        profilePic = Session.retreivePFUrl(getApplicationContext(),Session.FB_PROFILE_PIC_URL);
        fbName = Session.retreiveFbName(getApplicationContext(),Session.FB_PROFILE_NAME);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.backCelebMessage:
                intent = new Intent(MessageActivity.this, CelebHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
