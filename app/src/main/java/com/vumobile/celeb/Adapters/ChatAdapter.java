package com.vumobile.celeb.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vumobile.celeb.R;
import com.vumobile.celeb.model.ChatClass;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toukirul on 16/5/2017.
 */

public class ChatAdapter extends ArrayAdapter<ChatClass> {

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    private Context mContext;
    private ImageView img;
    private TextView txtChat;
    Matcher m;

    public ChatAdapter(Context context, int resource, List<ChatClass> items) {
        super(context, resource, items);
        this.mContext = context;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pattern p = Pattern.compile(URL_REGEX);

        View v = convertView;

        ChatClass requestClass = getItem(position);

        //  if (v == null) {
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (requestClass.getIsCeleb().equals("1")) {
            v = vi.inflate(R.layout.row_chat, null);
        } else {
            v = vi.inflate(R.layout.row_chat_right, null);
        }
        //   }


        if (requestClass != null) {

            TextView tt3 = (TextView) v.findViewById(R.id.txtChat);
            ImageView imgFan = (ImageView) v.findViewById(R.id.chatImage);
            ImageView imgChat = (ImageView) v.findViewById(R.id.imgChat);

            m = p.matcher(requestClass.getText());

            if (m.find()){
                tt3.setVisibility(View.GONE);
                imgChat.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(requestClass.getText()).thumbnail(0.5f).into(imgChat);
            }

            if (tt3 != null) {
                tt3.setText(requestClass.getText());
            }

            if (tt3 != null) {
                Picasso.with(mContext).load(requestClass.getImageUrl()).into(imgFan);
            }
        }

        return v;
    }


//    @Nullable
//    @Override
//    public ChatClass getItem(int position) {
//        return super.getItem(getCount() - position - 1);
//    }
}
