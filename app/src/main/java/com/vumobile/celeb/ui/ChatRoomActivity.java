package com.vumobile.celeb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {

    private List<ChatClass> chatClassList = new ArrayList<ChatClass>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatClass chatClass;
    private ImageView imageViewChatSend;
    private EditText editTextChatText;
    private String chatText, celebName, celebPic;
    private String room_name, temp_key, msisdn, profilePic, fbName;
    private CircleImageView circleImageViewChatProfilePic;
    private TextView textViewChatName;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private String chatName, chat_msg, isCeleb, imageUrl;//, image_url = "https://graph.facebook.com/1931218820457638/picture?width=500&height=500";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initUI();




        // dont create room here .. create room when confirm user for chat

        celebName = getIntent().getStringExtra("CELEB_NAME");
        celebPic = getIntent().getStringExtra("CELEB_PIC");
        room_name = getIntent().getStringExtra("room");
        msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        profilePic = Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL);
        fbName = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
        //room_name = "88014444444448801666666666";
        //room_name = "Room:5GAMB3EBCM";
        Log.d("room_name", room_name);
        Log.d("room_name", msisdn);
        Log.d("room_name", profilePic);
        Log.d("room_name", fbName);
        Log.d("celebPic", celebPic);

        //createRoomOnFirebase(room_name);
        Glide.with(this).load(celebPic).into(circleImageViewChatProfilePic);
        textViewChatName.setText(celebName);
        getAllComment(room_name);
    }

    private void initUI() {

        editTextChatText = (EditText) findViewById(R.id.editTextChatText);
        textViewChatName = (TextView) findViewById(R.id.textViewChatName);

        listView = (ListView) findViewById(R.id.listChat);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        imageViewChatSend = (ImageView) findViewById(R.id.imageViewChatSend);
        circleImageViewChatProfilePic = (CircleImageView) findViewById(R.id.circleImageViewChatProfilePic);
        imageViewChatSend.setOnClickListener(this);


        adapter = new ChatAdapter(getApplicationContext(), R.layout.row_chat, chatClassList);

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageViewChatSend:
                chatText = editTextChatText.getText().toString();
                postComment(chatText);

                // clear text box
                editTextChatText.setText("");
                //  listView.setSelection(adapter.getCount() - 1);

                break;
        }
    }

    private void postComment(String comment) {

        if (Session.isCeleb(getApplicationContext(), Session.IS_CELEB)) {
            isCeleb = "1"; // 1 is celeb 2 is fan
        } else {
            isCeleb = "2";
        }

        Map<String, Object> map = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference message_root = root.child(temp_key);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("msisdn", msisdn);
        map2.put("msg", comment);
        map2.put("imageUrl", profilePic);
        map2.put("fbName", fbName);
        map2.put("isCeleb", isCeleb);


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


    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            //String is = (String) ((DataSnapshot) i.next()).getValue();
            chatName = (String) ((DataSnapshot) i.next()).getValue();
            imageUrl = (String) ((DataSnapshot) i.next()).getValue();
            isCeleb = (String) ((DataSnapshot) i.next()).getValue();
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            room_name = (String) ((DataSnapshot) i.next()).getValue();
            Log.d("iscelelele", chat_msg);
            Log.d("iscelelele", room_name);
            Log.d("iscelelele", imageUrl);
            Log.d("iscelelele", chatName);

            ChatClass chatClass = new ChatClass();
            chatClass.setImageUrl(imageUrl);
            chatClass.setText(chat_msg);
            chatClass.setIsCeleb(isCeleb);
            //commentClass.setTime(getTime());

            chatClassList.add(chatClass);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
}
