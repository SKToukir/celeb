package com.vumobile.fan.login.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.utils.ChatBot;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.github.bassaer.chatmessageview.views.MessageView;
import com.vumobile.celeb.R;

import java.util.ArrayList;
import java.util.Random;

public class ChatViewActivity extends AppCompatActivity {

    ChatView mChatView;

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


        // User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        getSupportActionBar().setTitle(myName);
        getSupportActionBar().setIcon(R.drawable.face_2);

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        final User me = new User(myId, myName, myIcon);
        final User you = new User(yourId, yourName, yourIcon);


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

            //Receive message
            final Message receivedMessage = new Message.Builder()
                    .setUser(you)
                    .setRightMessage(false)
                    .setMessageText(ChatBot.talk(me.getName(), message.getMessageText()))
                    .build();

            // This is a demo bot
            // Return within 3 seconds
            int sendDelay = (new Random().nextInt(4) + 1) * 1000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChatView.receive(receivedMessage);
                }
            }, sendDelay);
        });


    } // end of onCreate

}
