package com.vumobile.celeb.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vumobile.Config.Api;
import com.vumobile.celeb.Adapters.ChatAdapter;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ChatClass;
import com.vumobile.celeb.model.ImageProcessingClass;
import com.vumobile.fan.login.ImageOrVideoView;
import com.vumobile.fan.login.Session;
import com.vumobile.fan.login.ui.fragment.Gifts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {

    public String type;
    public String fanMsisdn, celebMsisdn;
    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    private FrameLayout frameLayoutChatGift;
    private static final int CAMERA_REQUEST = 1888;
    private List<ChatClass> chatClassList = new ArrayList<ChatClass>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatClass chatClass;
    private ImageView imageViewChatSend, imageViewChatAdd, imageViewChatCamera, imageViewChatGift;
    private EditText editTextChatText;
    private String chatText, celebName, celebPic;
    private static String room_name, temp_key, msisdn, profilePic, fbName;
    private CircleImageView circleImageViewChatProfilePic;
    private TextView textViewChatName;
    public static final int IMAGE_PICKER_SELECT = 1;
    private Uri uri;
    private String filePath = null;
    private boolean isImage = false;
    private FragmentTransaction ft;
    private Matcher m;
    FragmentManager fm;

    public String CELEB_MSISDN;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://vuceleb.appspot.com/");
    private static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private static String chatName, chat_msg, isCeleb, imageUrl;//, image_url = "https://graph.facebook.com/1931218820457638/picture?width=500&height=500";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initUI();

        if (Session.isCeleb(ChatRoomActivity.this,Session.IS_CELEB)){
            imageViewChatGift.setVisibility(View.GONE);
        }

        // dont create room here .. create room when confirm user for chat

        celebName = getIntent().getStringExtra("CELEB_NAME");
        celebPic = getIntent().getStringExtra("CELEB_PIC");
        room_name = getIntent().getStringExtra("room");
        msisdn = Session.retreivePhone(getApplicationContext(), Session.USER_PHONE);
        profilePic = Session.retreivePFUrl(getApplicationContext(), Session.FB_PROFILE_PIC_URL);
        fbName = Session.retreiveFbName(getApplicationContext(), Session.FB_PROFILE_NAME);
        //room_name = "88014444444448801666666666";
        //room_name = "Room:5GAMB3EBCM"
        celebMsisdn = room_name.substring(0,13);
        fanMsisdn = room_name.substring(13);
        Log.d("room_name", room_name);
        Log.d("room_name", room_name.substring(0,13));
        Log.d("room_name", msisdn);
        Log.d("room_name", profilePic);
        Log.d("room_name", fbName);
        Log.d("celebPic", celebPic);

        //createRoomOnFirebase(room_name);
        Glide.with(this).load(celebPic).into(circleImageViewChatProfilePic);
        textViewChatName.setText(celebName);
        getAllComment(room_name);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chat = chatClassList.get(i).getText();
                Pattern p = Pattern.compile(URL_REGEX);
                m = p.matcher(chat);

                if (m.find()) {
                    Intent intent = new Intent(ChatRoomActivity.this, ImageOrVideoView.class);
                    intent.putExtra("IMG_OR_VID", "1");
                    intent.putExtra("IMG_OR_VID_URL", chat);
                    startActivity(intent);
                }
            }
        });

        boolean celebOrNot = Session.isCeleb(getApplicationContext(), Session.IS_CELEB);

        // 1 = celeb and 2 = fan

        if (celebOrNot){
            type="1";
        }else {
            type="2";
        }
    }

    private void initUI() {

        frameLayoutChatGift = (FrameLayout) findViewById(R.id.frameLayoutChatGift);
        editTextChatText = (EditText) findViewById(R.id.editTextChatText);
        textViewChatName = (TextView) findViewById(R.id.textViewChatName);
        imageViewChatGift = (ImageView) findViewById(R.id.imageViewChatGift);
        imageViewChatGift.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listChat);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        imageViewChatCamera = (ImageView) findViewById(R.id.imageViewChatCamera);
        imageViewChatCamera.setOnClickListener(this);
        imageViewChatAdd = (ImageView) findViewById(R.id.imageViewChatAdd);
        imageViewChatAdd.setOnClickListener(this);
        imageViewChatSend = (ImageView) findViewById(R.id.imageViewChatSend);
        circleImageViewChatProfilePic = (CircleImageView) findViewById(R.id.circleImageViewChatProfilePic);
        imageViewChatSend.setOnClickListener(this);

        adapter = new ChatAdapter(getApplicationContext(), R.layout.row_chat, chatClassList);
        listView.setAdapter(adapter);

        // get fragment manager
        fm = getSupportFragmentManager();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageViewChatSend:
                chatText = editTextChatText.getText().toString();

                if (!chatText.equals("")){

                    setNewMessageCount(Api.API_SET_NEW_MESSAGE_COUNT);

                    postComment(getApplicationContext(), chatText);
                }else {
                    Log.d("ChatText","Null");
                }
                // clear text box
                editTextChatText.setText("");
                //  listView.setSelection(adapter.getCount() - 1);
                break;
            case R.id.imageViewChatAdd:
                choose_from_gallery();
                break;
            case R.id.imageViewChatCamera:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            case R.id.imageViewChatGift:
                // frameLayoutChatGift.setVisibility(View.VISIBLE);
                ft = fm.beginTransaction();
                Log.d("ftft 1 ", "onClick: " + fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount() == 0) {

                    // add
                    ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
                    ft.add(R.id.frameLayoutChatGift, new Gifts());
                    ft.addToBackStack("ttt");
                    ft.commit();
                    Log.d("ftft 2 ", "onClick: " + fm.getBackStackEntryCount());
                }

                break;
        }
    }

    private void setNewMessageCount(String apiSetNewMessageCount) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiSetNewMessageCount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Fan", fanMsisdn);
                params.put("Celebrity", celebMsisdn);
                params.put("Flag", type);
                Log.d("fanmsisdn",fanMsisdn);
                Log.d("fanmsisdn",celebMsisdn);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    // pic image from gallery
    private void choose_from_gallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_SELECT) {
            //handle image
            uri = data.getData();
            postImage(uri);

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap b = (Bitmap) data.getExtras().get("data");
            uri = ImageProcessingClass.getImageUri(getApplicationContext(), b);
            postImage(uri);
        }
    }

    private void postImage(Uri uri) {
        isImage = true;

        StorageReference childRef = storageRef.child(getRealPathFromURI(getApplicationContext(), uri));

        //uploading the image
        UploadTask uploadTask = childRef.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri u = taskSnapshot.getMetadata().getDownloadUrl();
                Log.d("FileUploadLog", taskSnapshot.toString() + " " + u.toString());
                postComment(getApplicationContext(), u.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FileUploadLog", e.toString());
            }
        });
    }

    // this method call for get video file uri
    public String getRealPathFromURI(Context context, Uri contentUri){
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void postComment(Context context, String comment) {

        if (Session.isCeleb(context, Session.IS_CELEB)) {
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

        Log.d("cmt", "postComment: " + comment);

        message_root.updateChildren(map2);


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

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private void removeBadge() {

        String url = "http://wap.shabox.mobi/testwebapi/Celebrity/UpdateFanCelebCount?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FromServer", response.toString());
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
                Map<String, String> params = new HashMap<String, String>();

                String uType;

                if (Session.isCeleb(getApplicationContext(),Session.IS_CELEB)){
                    uType = "1";
                }else {
                    uType = "0";
                }

                params.put("Fan", fanMsisdn);
                params.put("Celebrity", celebMsisdn);
                params.put("Flag", uType);
                Log.d("lkdjalskdjasld",uType);




                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        removeBadge();
        super.onResume();
    }
}
