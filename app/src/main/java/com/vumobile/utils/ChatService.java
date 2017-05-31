package com.vumobile.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vumobile.notification.Utils;

import java.util.Iterator;

/**
 * Created by toukirul on 29/5/2017.
 */

public class ChatService extends Service {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("iscelelele", "chatName");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("iscelelele", "chatName");
        getAllComment("88017111111118801672150360");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
                Log.d("iscelelele", "New message");
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
           String chatName = (String) ((DataSnapshot) i.next()).getValue();
            String imageUrl = (String) ((DataSnapshot) i.next()).getValue();
            String isCeleb = (String) ((DataSnapshot) i.next()).getValue();
            String chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            String room_name = (String) ((DataSnapshot) i.next()).getValue();
            Log.d("iscelelele", chat_msg);
            Log.d("iscelelele", room_name);
            Log.d("iscelelele", imageUrl);
            Log.d("iscelelele", chatName);

            Utils.setCustomViewNotification(ChatService.this,chatName,room_name,imageUrl);
        }

    }
}
