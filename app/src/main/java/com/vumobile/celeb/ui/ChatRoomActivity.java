package com.vumobile.celeb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vumobile.celeb.Adapters.ChatAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ChatClass;
import com.vumobile.fan.login.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {

    private List<ChatClass> chatClassList = new ArrayList<ChatClass>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatClass chatClass;
    private Button btnSend;
    private EditText etChat;
    private String chatText;
    private String room_name, temp_key, image_url;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initUI();

        // dont create room here .. create room when confirm user for chat

        room_name = getIntent().getStringExtra("room");
        image_url = Session.retreivePFUrl(getApplicationContext(),Session.FB_PROFILE_PIC_URL);
        //room_name = "88014444444448801666666666";
        //room_name = "Room:5GAMB3EBCM";
        Log.d("room_name", room_name);

        //createRoomOnFirebase(room_name);



        getAllComment(room_name);
    }

    private void initUI() {

        etChat = (EditText) findViewById(R.id.etChat);
        listView = (ListView) findViewById(R.id.listChat);
        btnSend = (Button) findViewById(R.id.btnSendChat);
        btnSend.setOnClickListener(this);

        adapter = new ChatAdapter(getApplicationContext(),R.layout.row_chat,chatClassList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSendChat:
                chatText = etChat.getText().toString();
                postComment(chatText);
                break;
        }
    }

    private void postComment(String comment) {

        Map<String, Object> map = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference message_root = root.child(temp_key);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", image_url);
        map2.put("msg", comment);

        message_root.updateChildren(map2);

//        listOfComment.setSelection(adapter.getCount() - 1);

    }

//    private void sendText(String chatText) {
//
//        chatClass = new ChatClass();
//
//        chatClass.setText(chatText);
//        chatClass.setImageUrl("https://graph.facebook.com/1931218820457638/picture?width=500&height=500");
//
//        chatClassList.add(chatClass);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//    }

//    private void createRoomOnFirebase(String room_name) {
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put(room_name, "");
//        root.updateChildren(map);
//
//    }


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

    private String chat_msg;//, image_url = "https://graph.facebook.com/1931218820457638/picture?width=500&height=500";

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            image_url = (String) ((DataSnapshot) i.next()).getValue();

            ChatClass chatClass = new ChatClass();
            chatClass.setImageUrl(image_url);
            chatClass.setText(chat_msg);
            //commentClass.setTime(getTime());

            chatClassList.add(chatClass);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
}
