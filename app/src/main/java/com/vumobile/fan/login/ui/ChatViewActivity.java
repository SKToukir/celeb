package com.vumobile.fan.login.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
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
import com.vumobile.utils.MyInternetCheckReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChatViewActivity extends AppCompatActivity {

    ChatView mChatView;


    private List<ChatClass> chatClassList = new ArrayList<ChatClass>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatClass chatClass;
    private Button btnSend;
    private EditText etChat;
    private String chatText;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    LinearLayout content_chat_view;
    ArrayList<Message> messages;
    private String room_name, temp_key, msisdn, profilePic, fbName;
    private String chatName, chat_msg, isCeleb, imageUrl;//, image_url = "https://graph.facebook.com/1931218820457638/picture?width=500&height=500";

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

        content_chat_view = (LinearLayout)findViewById(R.id.content_chat_view);

// cra

//        ChatProfileModel chatProfileModelMe = (ChatProfileModel) getIntent().getSerializableExtra("PROFILE_INFO_ME");
//        ChatProfileModel chatProfileModelYou = (ChatProfileModel) getIntent().getSerializableExtra("PROFILE_INFO_YOU");
//       Bitmap bitmapMe = (Bitmap) this.getIntent().getParcelableExtra("PROFILE_IMG_ME");
//        Bitmap bitmapYou = (Bitmap) this.getIntent().getParcelableExtra("PROFILE_IMG_YOU");

    //    final User me = new User(0, chatProfileModelMe.getName(), null);


        // don't create room here .. create room when confirm user for chat
//        room_name = getIntent().getStringExtra("room");
//        msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
//        profilePic = Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL);
//        fbName = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);


    //    getAllComment(room_name, me);

// cra

        // User id
        int myId = 0;
        //User icon
        //  Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Michael";

        getSupportActionBar().setTitle(myName);

        int yourId = 1;
        //   Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";


        // all message in list
        messages = new ArrayList<>();



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
//                    .setUser(me2)
                    .setRightMessage(true)
                    .setMessageText(mChatView.getInputText())
                    .hideIcon(true)
                    .build();
            //Set to chat view
            mChatView.send(message);
            //Reset edit text


            //------------------------------------------------------
            // firebase post comment
            boolean isCeleb = Session.isCeleb(getApplicationContext(), Session.IS_CELEB);

            if (isCeleb) {
                postComment(mChatView.getInputText(), "1");
            } else {
                postComment(mChatView.getInputText(), "2");
            }
            //------------------------------------------------------

            mChatView.setInputText("");
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

        // show snackbar while no internet
        MyInternetCheckReceiver.isNetworkAvailableShowSnackbar(this, content_chat_view);
        new MyInternetCheckReceiver(content_chat_view);


    } // end of onCreate


    private void postComment(String comment, String isCele) {

        Map<String, Object> map = new HashMap<>();
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference message_root = root.child(temp_key);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("msisdn", msisdn);
        map2.put("msg", comment);
        map2.put("imageUrl", profilePic);
        map2.put("fbName", fbName);
        map2.put("isCeleb", isCele);

        message_root.updateChildren(map2);

//        listOfComment.setSelection(adapter.getCount() - 1);

    }

    // this method is used for only audience
    public void getAllComment(String test, User me) {
        root = FirebaseDatabase.getInstance().getReference().child(test);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    append_chat_conversation(dataSnapshot, me);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    append_chat_conversation(dataSnapshot, me);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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


    private void append_chat_conversation(DataSnapshot dataSnapshot, User me) throws ExecutionException, InterruptedException {


        Iterator i = dataSnapshot.getChildren().iterator();
        Message msg = null;
        while (i.hasNext()) {
            chatName = (String) ((DataSnapshot) i.next()).getValue();
            imageUrl = (String) ((DataSnapshot) i.next()).getValue();
            isCeleb = (String) ((DataSnapshot) i.next()).getValue();
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            room_name = (String) ((DataSnapshot) i.next()).getValue();

            msg = new Message();
            msg.setMessageText(chat_msg);

            Bitmap bm = getBitmapFromURL(getApplicationContext(), imageUrl);

            if (isCeleb.equals("1")) {
                final User you = new User(1, chatName, bm);
                msg.setUser(you);
            } else {
                msg.setUser(me);
                msg.setRightMessage(true);
            }

            msg.setDateCell(false);

            //    mChatView.send(msg);

            messages.add(msg);
            MessageView messageView = (MessageView) findViewById(R.id.message_view);
            messageView.init(messages);

        }


//    }


    }

    Bitmap bitmap = null;

    public Bitmap getBitmapFromURL(Context context, String src) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Bitmap> callable = new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws ExecutionException, InterruptedException {

                bitmap = Glide.with(context)
                        .load(src)
                        .asBitmap()
                        .into(30, 30)
                        .get();
                return bitmap;
            }
        };

        Future<Bitmap> future = executor.submit(callable);
        // future.get() returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();
        return bitmap;
    }


}
