package com.vumobile.fan.login.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.github.bassaer.chatmessageview.views.MessageView;
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

public class ChatViewActivity extends AppCompatActivity {

    ChatView mChatView;


    private List<ChatClass> chatClassList = new ArrayList<ChatClass>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatClass chatClass;
    private Button btnSend;
    private EditText etChat;
    private String chatText;
    private String room_name, temp_key, image_url;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    User me;
    User you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Implemented by activity
        });

// cra
//        etChat = (EditText) findViewById(R.id.etChat);
//        listView = (ListView) findViewById(R.id.listChat);
//        btnSend = (Button) findViewById(R.id.btnSendChat);
//        btnSend.setOnClickListener(this);

//        adapter = new ChatAdapter(getApplicationContext(), R.layout.row_chat, chatClassList);
//        listView.setAdapter(adapter);

        // dont create room here .. create room when confirm user for chat

        room_name = getIntent().getStringExtra("room");
        image_url = Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL);
        Log.d("room_name", room_name);

        getAllComment(room_name);


// cra


        // User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        getSupportActionBar().setTitle(myName);

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        me = new User(myId, myName, myIcon);
        you = new User(yourId, yourName, yourIcon);


        // all message in list
        Message messagea = new Message();
        messagea.setMessageText("hi");
        messagea.setUser(me);
        messagea.setRightMessage(true);
        messagea.setDateCell(true);


        Message message2 = new Message();
        message2.setMessageText("hello");
        message2.setUser(you);
        message2.setDateCell(true);

        Message message3 = new Message();
        message3.setMessageText("Ki khobor");
        message3.setUser(me);
        message3.setRightMessage(true);
        message3.setDateCell(false);

        Message message4 = new Message();
        message4.setMessageText("valo");
        message4.setUser(you);
        message4.setDateCell(false);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(messagea);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);
        MessageView messageView = (MessageView) findViewById(R.id.message_view);
        messageView.init(messages);


        mChatView = (ChatView) findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.myColorTwoHeader));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.myColorOneBody));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.myColorTwoHeader));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("New message...");
        mChatView.setMessageMarginTop(3);
        mChatView.setMessageMarginBottom(3);

        // Click Send Button
        mChatView.setOnClickSendButtonListener(view -> {


            // New message
            Message message = new Message.Builder()
                    .setUser(me)
                    .setRightMessage(true)
                    .setMessageText(mChatView.getInputText())
                    .hideIcon(true)
                    .build();
            //Set to chat view
            mChatView.send(message);
            //Reset edit text
            mChatView.setInputText("");

            //------------------------------------------------------
            // firebase post comment
            postComment(mChatView.getInputText());
            //------------------------------------------------------



//            //Receive message
//            final Message receivedMessage = new Message.Builder()
//                    .setUser(you)
//                    .setRightMessage(false)
//                    .setMessageText(ChatBot.talk(me.getName(), message.getMessageText()))
//                    .build();
//
//
//            // This is a demo bot
//            // Return within 3 seconds
//            int sendDelay = (new Random().nextInt(4) + 1) * 1000;
//
//
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mChatView.receive(receivedMessage);
//                }
//            }, sendDelay);











        });


    } // end of onCreate


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

        //    chatClassList.add(chatClass);

            //------------------------------------------------------------
            //Receive message
            final Message receivedMessage = new Message.Builder()
                    .setUser(you)
                    .setRightMessage(false)
                    .setMessageText(chat_msg)
                    .build();

            mChatView.receive(receivedMessage);
            //------------------------------------------------------------

        }
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();




    }


}
